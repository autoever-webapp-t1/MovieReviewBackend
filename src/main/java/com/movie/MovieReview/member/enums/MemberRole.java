package com.movie.MovieReview.member.enums;

import lombok.Getter;

@Getter
public enum MemberRole {
    USER("user"),
    ADMIN("admin");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }
}
