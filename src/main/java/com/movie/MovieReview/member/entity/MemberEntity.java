package com.movie.MovieReview.member.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId; //카카오 고유 사용자 ID

    private String nickname; //카카오 사용자 이름

    private String email; //카카오 사용자 이메일

    private String profile; //카카오 사용자 프로필사진

    private String refreshToken; //카카오 사용자 refreshToken
}
