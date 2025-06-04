package com.vueart.api.config.security.jwt;

import com.vueart.api.common.auth.dto.CustomOauth2UserDetails;
import com.vueart.api.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.debug("Invalid JWT token");
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

//        if (redisService.checkBlackList(token)) {
//            log.debug("JWT token is blacklisted");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }

        if (jwtTokenProvider.validateToken(token)) {
            log.debug("JWT token validated");
            User user;
            Long userId = jwtTokenProvider.getUserIdFromToken(token);
            String email = jwtTokenProvider.getEmailFromToken(token);

            Map<String, Object> attributes = jwtTokenProvider.getAttributesFromToken(token);
            CustomOauth2UserDetails userDetails;
            if (attributes != null && !attributes.isEmpty()) {
                String name = "Unknown User";
                if (attributes.containsKey("kakao_account")) {
                    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                    if (kakaoAccount.containsKey("profile")) {
                        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
                        if (profile.containsKey("nickname")) {
                            name = profile.get("nickname").toString();
                        }
                    }
                } else if (attributes.containsKey("name")) {
                    // Google의 경우 name 속성 사용
                    name = attributes.get("name").toString();
                }

                userDetails = new CustomOauth2UserDetails(User.builder()
                        .id(userId)
                        .email(email)
                        .userName(name)
                        .build(), attributes);
            } else {
                user = User.builder()
                        .id(userId)
                        .email(email)
                        .build();
                userDetails = new CustomOauth2UserDetails(
                        user,
                        attributes != null ? attributes : Collections.emptyMap()
                );
            }

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
