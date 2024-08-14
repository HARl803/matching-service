package com.haribo.matching_service.mentee.application.service;

import com.haribo.matching_service.global.enums.ReservationStatus;
import com.haribo.matching_service.mentee.domain.Reservation;
import com.haribo.matching_service.mentee.domain.ReservationDocs;
import com.haribo.matching_service.mentee.domain.repository.ReservationDocsRepository;
import com.haribo.matching_service.mentee.domain.repository.ReservationRepository;
import com.haribo.matching_service.mentee.presentation.request.MenteePossiRequest;
import com.haribo.matching_service.mentee.presentation.response.ProfileMemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MenteeServiceImpl implements MenteeService{
    private final ReservationDocsRepository reservationDocsRepository;
    private final ReservationRepository reservationRepository;

    private final String loginMemberUrl = "http://localhost:8080/api/v1/auth/profile";

    private static final Logger logger = LoggerFactory.getLogger(MenteeServiceImpl.class);
    @Override
    public void createReservation(MenteePossiRequest menteePossiRequest) {

        logger.info("1. 로그인한 사용자 아이디를 가져온다.");
        RestTemplate restTemplate = new RestTemplate();
        ProfileMemberResponse profileMemberResponse = restTemplate.getForObject(loginMemberUrl, ProfileMemberResponse.class);

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
        assert profileMemberResponse != null;
        String menteeId = profileMemberResponse.getProfileMember().getProfileId();
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

        logger.info("4. NoSQL에 저장한다. (reservationId, mentorId, status, request 알고있음)");
        ReservationDocs.MenteeAvailableTimes menteeAvailableTimes = ReservationDocs.MenteeAvailableTimes
                .builder()
                .menteeId(menteeId)
                .possibleStartTimes(menteePossiRequest.getPossibleStartTime())
                .build();

        ReservationDocs.Log log = ReservationDocs.Log
                .builder()
                .status(ReservationStatus.RESERVATION_PENDING)
                .memberId(menteeId)
                .creationDate(LocalDateTime.now())
                .build();

        ReservationDocs complete = ReservationDocs
                .builder()
                .reservationId(reservationId)
                .menteeAvailableTimes(menteeAvailableTimes)
                .log(log)
                .build();
        reservationDocsRepository.save(complete);
    }
}
