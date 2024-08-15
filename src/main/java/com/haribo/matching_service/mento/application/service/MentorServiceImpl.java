package com.haribo.matching_service.mento.application.service;

import com.haribo.matching_service.global.enums.ReservationStatus;
import com.haribo.matching_service.mentee.domain.Reservation;
import com.haribo.matching_service.mentee.domain.ReservationDocs;
import com.haribo.matching_service.mentee.domain.repository.ReservationDocsRepository;
import com.haribo.matching_service.mentee.domain.repository.ReservationRepository;
import com.haribo.matching_service.mento.domain.MentorDocs;
import com.haribo.matching_service.mento.domain.repository.MentorDocsRepository;
import com.haribo.matching_service.mento.presentation.request.MatchingConfirmedRequest;
import com.haribo.matching_service.mento.presentation.response.MemberIdResponse;
import com.haribo.matching_service.mento.presentation.response.MentorPossiTimesResponse;
import com.mongodb.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {
    private final MentorDocsRepository mentorDocsRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDocsRepository reservationDocsRepository;
    private final MongoTemplate customMongoTemplate = new MongoTemplate(MongoClients.create("mongodb://ssafy:ssafy@i11a803.p.ssafy.io:27017/haribo?authSource=admin"), "haribo");
    private static final Logger logger = LoggerFactory.getLogger(MentorServiceImpl.class);

    @Override
    public List<Long> getMentorPossiTime(String mentorId) {
        /*
            TODO : 멘토 가능시간 받아오기
            (해당 멘토의 가능시간(비트연산) 받아오고) - (멘티예약 중 mentorId가 멘토인 예약확정, 컴터챗확정)
            "채팅대기","채팅입장","매칭실패","매칭완료" => "결제"
         */

        Query query = new Query(Criteria.where("profile_id").is(mentorId));
        logger.info("Executing query: {}", query);
        logger.info("Current MongoDB Database: "+ customMongoTemplate.getDb().getName());

        MentorDocs result = customMongoTemplate.findOne(query, MentorDocs.class);

        List<Long> updatedAvailability;
        if (result != null) {
            List<Long> possiMentorTime = result.getTimes();
            logger.info("getTimes : " + possiMentorTime);

            logger.info("1. reservation log 보면서 결제만 찾고,");

            logger.info("2. 해당 상태인데 멘토아이디가 맞는 시간대를 다 가져온다.");
            Query logQuery = new Query();
            logQuery.addCriteria(Criteria.where("log").exists(true));

            logger.info("MongoDB에서 쿼리 실행");
            List<ReservationDocs> results = customMongoTemplate.find(logQuery, ReservationDocs.class);

            logger.info("조건에 맞는 creation_date 추출");
            List<LocalDateTime> creationDates = new ArrayList<>();
            for (ReservationDocs doc : results) {
                Map<String, ReservationDocs.Log> logMap = doc.getLog();
                for (ReservationDocs.Log logEntry : logMap.values()) {
                    if (Objects.equals(ReservationStatus.PAYMENT.getKorean(), logEntry.getStatus()) && mentorId.equals(logEntry.getMemberId())) {
                        // LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString);

//                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
//                        ZonedDateTime zonedDateTime = ZonedDateTime.parse(logEntry.getCreationDate(), formatter);

                        creationDates.add(LocalDateTime.parse(logEntry.getCreationDate()));
                    }
                }
            }
            logger.info("2-1. creationDates(찾기에 따른 결과(시간) 모음) : " + creationDates);

            logger.info("MongoDB에서 조회된 문서 수: " + results.size());


            logger.info("3. 시간대를 보고 요일로 치환");
            updatedAvailability = new ArrayList<>(possiMentorTime);

            for (LocalDateTime dateTime : creationDates) {
                int dayOfWeek = dateTime.get(ChronoField.DAY_OF_WEEK) - 1;

                int hour = dateTime.getHour();
                int minute = dateTime.getMinute();
                int slot = (hour * 2) + (minute >= 30 ? 1 : 0);

                logger.info("지금 조회된 요일, 그릐고 시간과 분 => ("+dayOfWeek+") "+hour+" : "+minute);
                long currentDayAvailability = updatedAvailability.get(dayOfWeek);
                long mask = ~(1L << (48-slot));

                logger.info("슬롯? 과 currentDayAvailability, mask => ("+slot+") "+ currentDayAvailability+" "+Long.toBinaryString(mask));
                logger.info("4. 저장된 숫자들과 비트연산 & : " + Long.toBinaryString(currentDayAvailability & mask));
                updatedAvailability.set(dayOfWeek, currentDayAvailability & mask);
            }
        } else {
            throw new RuntimeException("Profile not found");
        }

        logger.info("4. updatedAvailability : " + updatedAvailability);

        return updatedAvailability;
    }

    @Override
    public void fixMentorPossiTime(MatchingConfirmedRequest matchingConfirmedRequest) {
        Integer matchingId = Integer.parseInt(matchingConfirmedRequest.getMatchingId());
        String possibleStartTime = matchingConfirmedRequest.getPossibleStartTime();

        logger.info("1. MySQL에 예약 확정으로 update");

        logger.info("1-1. MySQL에 matchingId로 Reservation 객체 가져오기");
        Optional<Reservation> reservationOptional = reservationRepository.findById(matchingId);
        if(reservationOptional.isEmpty()){
            throw new RuntimeException("reservationOptional 오류에요");
        }

        logger.info("1-2. 결제대기 상태로 변경");
        Reservation reservationUpdate = reservationOptional.get();
        String menteeId = reservationUpdate.getMenteeId();
        String mentorId = reservationUpdate.getMentoId();

        Reservation updatedReservation = reservationUpdate.toBuilder()
                .status(ReservationStatus.PAYMENT)
                .build();
        reservationRepository.save(updatedReservation);
        logger.info("2. MongoDB에 예약 현황 업데이트");

        Query reservationQuery = new Query(Criteria.where("reservation_id").is(matchingId));
        logger.info("Executing query: {}", reservationQuery);
        ReservationDocs reservationDocs = customMongoTemplate.findOne(reservationQuery, ReservationDocs.class);
        logger.info("Current MongoDB Database: "+ customMongoTemplate.getDb().getName());

        logger.info("2-1. ReservationDocs :"+ reservationDocs);
        if (reservationDocs != null) {
            Map<String, ReservationDocs.Log> logMap = reservationDocs.getLog();
            logger.info("2-2. logMap : " + logMap);

            ReservationDocs.Log newLog = ReservationDocs.Log.from(ReservationStatus.PAYMENT, menteeId, LocalDateTime.now());
            logMap.put(String.valueOf(UUID.randomUUID()), newLog);

            reservationDocs.setLog(logMap);

            ReservationDocs savedDocs = reservationDocsRepository.save(reservationDocs);
            logger.info("Updated ReservationDocs: " + savedDocs.toString());
        } else {
            throw new RuntimeException("Profile not found");
        }

        logger.info("3. Noti 에 전달");

        String apiUrl = "https://i11803.ssafy.io/api/v1/notification";

        logger.info("요청 헤더 및 본문 설정");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("receiver", menteeId);
        requestBody.put("add", String.valueOf(matchingId));
        requestBody.put("typeId", "NT01");

        logger.info("POST 요청 보내기");
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        logger.info("POST 요청 보내기");
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);

        logger.info("응답 상태 확인 및 처리");
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("알림이 잘못왔다네요 껄껄");
        }

        /*
        POST api : "/api/v1/notification"
        NotificationRequest
        (String receiver, // 수신자(즉 게시글 author ID)
        String add,      // 게시글 ID
        String content,  // 알람 내용인데 걍 무시하시면 됩니다.
        String typeId)   // “NT01~04” 중 하나
        */
    }

    @Override
    public MemberIdResponse getMemberId(String matchingId) {
        Optional<Reservation> reservationOptional = reservationRepository.findById(Integer.parseInt(matchingId));
        if(reservationOptional.isEmpty()){
            throw new RuntimeException("저장된 matchingId 없어요");
        }

        Reservation reservation = reservationOptional.get();

        return MemberIdResponse.builder()
                .mentorId(reservation.getMentoId())
                .menteeId(reservation.getMenteeId())
                .build();
    }
}
