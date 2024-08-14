package com.haribo.matching_service.mentee.application.service;

import com.haribo.matching_service.global.enums.ReservationStatus;
import com.haribo.matching_service.mentee.domain.Reservation;
import com.haribo.matching_service.mentee.domain.ReservationDocs;
import com.haribo.matching_service.mentee.domain.repository.ReservationDocsRepository;
import com.haribo.matching_service.mentee.domain.repository.ReservationRepository;
import com.haribo.matching_service.mentee.presentation.request.MenteePossiRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenteeServiceImpl implements MenteeService{
    private final ReservationDocsRepository reservationDocsRepository;
    private final ReservationRepository reservationRepository;
    private final MongoTemplate mongoTemplate;

    @Value("${base.login-member-url}")
    private String loginMemberUrl;

    private static final Logger logger = LoggerFactory.getLogger(MenteeServiceImpl.class);
    @Override
    public void createReservation(MenteePossiRequest menteePossiRequest) {

        logger.info("1. 로그인한 사용자 아이디를 가져온다.");
        // RestTemplate restTemplate = new RestTemplate();
        // ProfileMemberResponse profileMemberResponse = restTemplate.getForObject(loginMemberUrl, ProfileMemberResponse.class);

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Cookie", "JSESSIONID=11CD4A2080E246997063A5AE2207E99C");
//
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//        ResponseEntity<ProfileMemberResponse> response = restTemplate.exchange(
//                loginMemberUrl,
//                HttpMethod.GET,
//                entity,
//                ProfileMemberResponse.class
//        );

//        ProfileMemberResponse profileMemberResponse = response.getBody();

        logger.info("1-1. 꺄아");
        // assert profileMemberResponse != null;
        // String menteeId = profileMemberResponse.getProfileMember().getProfileId();
        String menteeId = "e5eb8557-729d-4e35-9782-c8219f6f1bbe";
        Reservation reservation = Reservation.builder()
                .mentoId(menteePossiRequest.getMentorId())
                .menteeId(menteeId)
                .status(ReservationStatus.RESERVATION_PENDING)
                .request(menteePossiRequest.getSymptomDetail())
                .build();

        logger.info("2. RDB 에 저장한다.");
        Reservation savedReservation = reservationRepository.save(reservation);

        logger.info("3. 저장한 예약 아이디를 가져온다.");
        Integer reservationId = savedReservation.getReservationId();
        logger.info("3-1. "+reservationId);

        logger.info("4. NoSQL에 저장한다. (reservationId, mentorId, status, request 알고있음)");
        ReservationDocs.MenteeAvailableTimes menteeAvailableTimes = ReservationDocs.MenteeAvailableTimes
                .builder()
                .menteeId(menteeId)
                .possibleStartTimes(menteePossiRequest.getPossibleStartTime())
                .build();

        Map<String, ReservationDocs.MenteeAvailableTimes> menteeAvailableTimesMap = Map.of( UUID.randomUUID().toString(), menteeAvailableTimes);

        logger.info("4-1. "+ menteeAvailableTimes.toString());
        ReservationDocs.Log log = ReservationDocs.Log.from(ReservationStatus.RESERVATION_PENDING, menteeId, LocalDateTime.now());

        Map<String, ReservationDocs.Log> logMap = Map.of( UUID.randomUUID().toString(), log);

        logger.info("4-2. "+ log.toString());
        ReservationDocs complete = ReservationDocs
                .builder()
                .reservationId(reservationId)
                .menteeAvailableTimes(menteeAvailableTimesMap)
                .log(logMap)
                .build();

        logger.info("4-3. "+ complete.toString());

        logger.info("Current MongoDB Database: " + mongoTemplate.getDb().getName());
        ReservationDocs savedDocs = reservationDocsRepository.save(complete);
        logger.info("Saved ReservationDocs: " + savedDocs.toString());

        logger.info("4-4. "+ savedDocs.toString());
    }
}
