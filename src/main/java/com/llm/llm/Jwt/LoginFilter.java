package com.llm.llm.Jwt;

//import io.github.resilience4j.ratelimiter.RateLimiter;
//import io.github.resilience4j.ratelimiter.RateLimiterConfig;
//import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    //private static final ConcurrentHashMap<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();


    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

        this.setUsernameParameter("userId");
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{


        //클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        System.out.println(username);
        System.out.println(password);

        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 DTO에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        System.out.println(authToken);
        //토큰에 담은 검증을 위한 매니저로 전달
        Authentication authResult = authenticationManager.authenticate(authToken);
        System.out.println("authResult: " + authResult);
        return authResult;
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {

        // 유저 정보
        String username = authResult.getName();

        Collection<? extends GrantedAuthority> authorities = authResult.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();


        response.setContentType("application/json");// JSON 응답임을 명시
        response.setCharacterEncoding("UTF-8"); // UTF-8 설정


        //토큰 생성
        String access = jwtUtil.createJwt("access", username, role,  36000000L); // 1시간
        String refresh = jwtUtil.createJwt("refresh", username, role,  2592000000L); // 3일


        response.setHeader("Authorization", "Bearer " + access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException faild) {
        response.setStatus(462);
    }

    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        return cookie;
    }

//    private String getClientIp(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip == null || ip.isEmpty()) {
//            ip = request.getRemoteAddr();
//        }
//        return ip;
//    }
//
//    private boolean isRequestAllowed(String clientIp) {
//        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(clientIp, this::createRateLimiter);
//        return rateLimiter.acquirePermission();
//    }
//
//    private RateLimiter createRateLimiter(String clientIp) {
//        RateLimiterConfig config = RateLimiterConfig.custom()
//                .limitForPeriod(5) // 5분 동안 최대 5번 요청 가능
//                .limitRefreshPeriod(java.time.Duration.ofMinutes(5))
//                .timeoutDuration(java.time.Duration.ofMillis(0))
//                .build();
//        return RateLimiter.of(clientIp, config);
//    }
}

