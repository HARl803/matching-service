package com.haribo.matching_service.mento.presentation;

import com.haribo.matching_service.mento.application.service.MentorService;
import com.haribo.matching_service.mento.presentation.request.MatchingConfirmedRequest;
import com.haribo.matching_service.mento.presentation.response.MemberIdResponse;
import com.haribo.matching_service.mento.presentation.response.MentorPossiTimesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentor")
@Slf4j
public class MentorController {
    private final MentorService mentorService;

    @GetMapping("/get-possi-time/{mentorId}")
    public ResponseEntity<List<Long>> getMentorPossiTimes(@PathVariable("mentorId") String mentorId){
        log.info("1. 멘토 가능시간 부르는 API 호출");
        return ResponseEntity.ok(mentorService.getMentorPossiTime(mentorId));
    }

    @GetMapping("/fix-confirmed-time")
    public ResponseEntity<Void> fixMentorPossiTimes(@RequestBody MatchingConfirmedRequest matchingConfirmedRequest){
        log.info("2. 확정된 멘토 일정 저장하고 멘티에게 알림보내는 API 호출");
        mentorService.fixMentorPossiTime(matchingConfirmedRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{matchingId}")
    public ResponseEntity<MemberIdResponse> getMemberId(@PathVariable("matchingId") String matchingId){
        log.info("3. 매칭 아이디로 멘토와 멘티 아이디 가져오는 API 호출");
        return ResponseEntity.ok(mentorService.getMemberId(matchingId));
    }



}
