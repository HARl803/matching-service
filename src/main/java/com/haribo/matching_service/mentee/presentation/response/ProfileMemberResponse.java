package com.haribo.matching_service.mentee.presentation.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ProfileMemberResponse {

    private ProfileMember profileMember;

    @Builder
    @Getter
    public static class ProfileMember {
        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private String profileId;
        private String name;
        private String nickName;
        private String email;
        private String profileImage;
        private String memberStatus;
    }
}
