package com.haribo.matching_service.global.enums;

import lombok.Getter;

@Getter
public enum ReservationStatus {
    RESERVATION_PENDING("예약대기"),
    TIME_SELECTION("시간선택"),
    PAYMENT("결제"),
    CHAT_PENDING("채팅대기"),
    CHAT_ENTRY("채팅입장"),
    MATCHING_FAILED("매칭실패"),
    MATCHING_COMPLETED("매칭완료");

    private final String korean;

    ReservationStatus(String korean) {
        this.korean = korean;
    }

}
