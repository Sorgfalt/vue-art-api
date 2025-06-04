package com.vueart.api.service.auth;

import com.vueart.api.common.redis.RedisService;
import com.vueart.api.common.util.AES256Util;
import com.vueart.api.config.security.jwt.TokenProvider;
import com.vueart.api.core.enums.Code;
import com.vueart.api.core.exception.VueArtApiException;
import com.vueart.api.dto.request.user.SignInRequest;
import com.vueart.api.dto.request.user.SignUpRequest;
import com.vueart.api.dto.response.auth.TokenDto;
import com.vueart.api.entity.User;
import com.vueart.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AES256Util aes256Util;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;

    @Override
    @Transactional
    public void signUp(SignUpRequest req) {
        if (req.email().isEmpty() || req.password().isEmpty()) {
            throw new VueArtApiException(Code.ErrorCode.INVALID_INPUT_VALUE);
        }

        if (userRepository.findByEmail(req.email()).isPresent()) {
            throw new VueArtApiException(Code.ErrorCode.ALREADY_REGISTERED_USER);
        }

        User user = User.builder()
                .email(req.email())
                .userId(req.userId())
                .password(passwordEncoder.encode(aes256Util.decode(req.password())))
                .business(Code.YN.N)
                .region(req.region())
                .role(Code.Role.USER)
                .build();
        userRepository.save(user);
    }

    @Override
    public TokenDto signIn(SignInRequest req) {
        String decPassword = null;
        boolean isValidPass = true;
        User user = userRepository.findByUserId(req.userId())
                .orElseThrow(() -> new VueArtApiException(Code.ErrorCode.NOT_REGISTERED_USER));
        System.out.print("debug");
        try { // 패스워드 암호화 위변조 체크
            decPassword = aes256Util.decode(req.password());
            log.info("decPassword = > {}", decPassword);
        } catch (Exception e) {
            isValidPass = false;
        }

        log.info("password => {}", user.getPassword());
        log.info("isValidPass => {}", isValidPass);

        if (!isValidPass || !passwordEncoder.matches(decPassword, user.getPassword())) {
            throw new VueArtApiException(Code.ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = tokenProvider.createAccessToken(user.getId(), user.getEmail(), null);
        String refreshToken = tokenProvider.createRefreshToken(user.getId(), user.getEmail());

        redisService.saveRefreshToken(user.getId(), refreshToken);

        return new TokenDto(accessToken, refreshToken);
    }
}
