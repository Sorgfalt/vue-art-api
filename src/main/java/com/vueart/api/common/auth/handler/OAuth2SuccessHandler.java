package com.vueart.api.common.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vueart.api.common.auth.dto.CustomOauth2UserDetails;
import com.vueart.api.common.redis.RedisService;
import com.vueart.api.common.response.CommonApiResponse;
import com.vueart.api.common.util.ApiResponseUtil;
import com.vueart.api.config.security.jwt.TokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final RedisService redisService;
    private final TokenProvider jwtTokenProvider;
    private final ApiResponseUtil apiResponseUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOauth2UserDetails userDetails = (CustomOauth2UserDetails) authentication.getPrincipal();

        Long userId = userDetails.getUserId();
        String email = userDetails.getEmail();
        Map<String, Object> attributes = userDetails.getAttributes();

        String accessToken = jwtTokenProvider.createAccessToken(userId, email, attributes);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId, email);

        redisService.saveRefreshToken(userId, refreshToken);

        // 응답 객체 생성
        Map<String, String> tokens = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
        CommonApiResponse<Map<String, String>> successResponse = apiResponseUtil.success(tokens);

        // 응답 작성
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(successResponse));
    }
}
