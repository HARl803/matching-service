package com.haribo.matching_service.mentee.presentation;

import com.haribo.matching_service.mentee.application.service.MenteeService;
import com.haribo.matching_service.mentee.presentation.request.MenteePossiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mentee")
public class MenteeController {
    private final MenteeService menteeService;

    @PostMapping("/reservation")
    public ResponseEntity<Void> createReservation(@RequestBody MenteePossiRequest menteePossiRequest) {
        menteeService.createReservation(menteePossiRequest);
        return ResponseEntity.ok().build();
    }

}
