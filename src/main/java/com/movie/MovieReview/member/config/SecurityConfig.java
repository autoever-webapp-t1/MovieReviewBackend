package com.movie.MovieReview.member.config;

import com.movie.MovieReview.member.service.JwtTokenService;
import com.movie.MovieReview.member.service.MemberService;
import com.movie.MovieReview.member.filter.JwtFilter;
import com.movie.MovieReview.exception.ExceptionHandlerFilter;
import com.movie.MovieReview.member.enums.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final JwtTokenService jwtTokenService;
    private final MemberService memberService;

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
//        System.out.println("region:"+region);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");  // Allow all origins, but you can restrict it to specific domains
        config.addAllowedMethod("*");  // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)
        config.addAllowedHeader("*");  // Allow all headers
        source.registerCorsConfiguration("/**", config);
        return source;
    }


    @Bean
    public SecurityFilterChain configure(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults()).csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(
                        (authorize) -> authorize.requestMatchers("/**").permitAll()
                                .requestMatchers("/user/**")
                                .hasAuthority(MemberRole.USER.getRole()).anyRequest().authenticated())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(httpSecurityFormLoginConfigurer -> httpSecurityFormLoginConfigurer.disable()) // 로그인 폼 미사용
                .httpBasic(httpSecurityHttpBasicConfigurer -> httpSecurityHttpBasicConfigurer.disable()) // http basic
                // 미사용
                .addFilterBefore(new JwtFilter(jwtTokenService, memberService),
                        UsernamePasswordAuthenticationFilter.class) // JWT Filter 추가
                .addFilterBefore(new ExceptionHandlerFilter(), JwtFilter.class) // Security Filter 에서 CustomException
                // 사용하기 위해 추가
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 아래 url은 filter 에서 제외
        return web -> web.ignoring().requestMatchers("/**");
    }
}
