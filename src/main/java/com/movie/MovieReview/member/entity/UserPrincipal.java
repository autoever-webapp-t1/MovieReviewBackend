//package com.movie.MovieReview.member.entity;
//
//import com.movie.MovieReview.member.dto.MemberDto;
//import com.movie.MovieReview.member.enums.MemberRole;
//import lombok.*;
//import java.util.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//
//@SuppressWarnings("serial")
//@Getter
//@AllArgsConstructor
//public class UserPrincipal implements UserDetails {
//
//    private Long id;
//    private String email;
//    private String password;
//    private Collection<? extends GrantedAuthority> authorities;
//    @Setter
//    private Map<String, Object> attributes;
//
//    public static UserPrincipal create(MemberDto memberDto) {
//        List<GrantedAuthority> authorities = Collections
//                .singletonList(new SimpleGrantedAuthority(MemberRole.USER.getRole()));
//        return new UserPrincipal(memberDto.getMemberId(), memberDto.getEmail(), "", authorities, null);
//    }
//
//    @Override
//    public boolean isAccountNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isAccountNonLocked() {
//        return true;
//    }
//
//    @Override
//    public boolean isCredentialsNonExpired() {
//        return true;
//    }
//
//    @Override
//    public boolean isEnabled() {
//        return true;
//    }
//
//    @Override
//    public String getUsername() {
//        return email;
//    }
//
//}
