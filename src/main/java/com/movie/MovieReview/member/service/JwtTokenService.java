package com.movie.MovieReview.member.service;

import java.lang.reflect.Member;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.movie.MovieReview.exception.CustomException;
import com.movie.MovieReview.exception.ErrorCode;
import com.movie.MovieReview.member.dto.MemberDto;
import com.movie.MovieReview.member.entity.MemberEntity;
import com.movie.MovieReview.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtTokenService implements InitializingBean {
    private long accessTokenExpirationInSeconds;
    private long refreshTokenExpirationInSeconds;
    private final String secretKey;
    private static Key key;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    public JwtTokenService(@Value("${jwt.access.token.expiration.seconds}") long accessTokenExpirationInSeconds,
                           @Value("${jwt.refresh.token.expiration.seconds}") long refreshTokenExpirationInSeconds,
                           @Value("${jwt.token.secret-key}") String secretKey) {
        this.accessTokenExpirationInSeconds = accessTokenExpirationInSeconds * 1000;
        this.refreshTokenExpirationInSeconds = refreshTokenExpirationInSeconds * 1000;
        this.secretKey = secretKey;
    }

    // 빈 주입받은 후 실행되는 메소드
    @Override
    public void afterPropertiesSet() {
        this.key = getKeyFromBase64EncodedKey(encodeBase64SecretKey(secretKey));
    }

    public String createJWTToken(String accessToken, String refreshToken, MemberDto memberDto) {
        return createToken(accessToken, refreshToken, memberDto, accessTokenExpirationInSeconds);
    }

    public String createToken(String accessToken, String refreshToken, MemberDto memberDto, long expireLength) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + expireLength);

        System.out.println("JWTTokenService??????????????? : " + memberDto);

        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", memberDto.getMemberId());
        claims.put("email", memberDto.getEmail());
        claims.put("nickname", memberDto.getNickname());
        claims.put("profile", memberDto.getProfile());
        claims.put("accessToken", accessToken);
        claims.put("refreshToken", refreshToken);

        return Jwts.builder().setClaims(claims) // 사용자 정보 포함
                .setSubject(String.valueOf(memberDto.getMemberId()))
                .setIssuedAt(now) // 발행 시간
                .setExpiration(validity) // 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명 알고리즘 및 비밀키 설정
                .compact();
    }

    //현재 payload에 담겨 있는 값은 memberId JWTToken에서 memberId추출
    public String getPayload(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getSubject();
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }

    public boolean validateToken(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);

            // 토큰의 만료 여부 확인
            boolean isValid = !claimsJws.getBody().getExpiration().before(new Date());
            System.out.println("JwtTokenService 토큰 유효!!!!!: " + isValid); // 토큰 유효성 출력
            return isValid;
        } catch (ExpiredJwtException e) {
            System.out.println("JwtTokenService 토큰 만료ㅜㅜㅜㅜㅜ: " + e.getMessage()); // 만료된 토큰 예외 출력
            return false;
        } catch (JwtException | IllegalArgumentException exception) {
            System.out.println("JwtTokenService 토큰 오류????: " + exception.getMessage()); // 유효하지 않은 토큰 예외 출력
            return false;
        }
    }

    private String encodeBase64SecretKey(String secretKey) {
        return Encoders.BASE64.encode(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    private Key getKeyFromBase64EncodedKey(String base64EncodedSecretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        return key;
    }
}