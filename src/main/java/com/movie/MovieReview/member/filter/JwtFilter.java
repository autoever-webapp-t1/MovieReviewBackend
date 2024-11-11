//package com.movie.MovieReview.member.filter;
//
//import com.movie.MovieReview.exception.CustomException;
//import com.movie.MovieReview.exception.ErrorCode;
//import com.movie.MovieReview.member.dto.MemberDto;
//import com.movie.MovieReview.member.entity.UserPrincipal;
//import com.movie.MovieReview.member.service.JwtTokenService;
//import com.movie.MovieReview.member.service.MemberService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.filter.GenericFilterBean;
//
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.GenericFilterBean;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JwtFilter extends GenericFilterBean {
//    public static final String AUTHORIZATION_HEADER = "Authorization";
//    private final JwtTokenService jwtTokenService;
//    private final MemberService memberService;
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
//        logger.info("[JwtFilter] : " + httpServletRequest.getRequestURL().toString());
//        String jwt = resolveToken(httpServletRequest);
//
//        if (StringUtils.hasText(jwt) && jwtTokenService.validateToken(jwt)) {
//            Long userId = Long.valueOf(jwtTokenService.getPayload(jwt));
//            MemberDto memberDto = memberService.findById(userId);
//            if (memberDto == null) {
//                throw new CustomException(ErrorCode.NOT_EXIST_USER);
//            }
//            UserDetails userDetails = UserPrincipal.create(memberDto);
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
//                    null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//        } else {
//            throw new CustomException(ErrorCode.INVALID_ACCESS_TOKEN);
//        }
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//
//    // GET Header Access Token
//    private String resolveToken(HttpServletRequest request) {
//        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
//
//        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//            return bearerToken.substring(7);
//        }
//
//        return null;
//    }
//}
