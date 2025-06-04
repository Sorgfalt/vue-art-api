package com.vueart.api.common.auth;

import com.vueart.api.common.auth.dto.CustomOauth2UserDetails;
import com.vueart.api.common.auth.dto.GoogleUserDetails;
import com.vueart.api.common.auth.dto.KakaoUserDetails;
import com.vueart.api.common.auth.dto.OAuth2UserInfo;
import com.vueart.api.core.enums.Code;
import com.vueart.api.entity.User;
import com.vueart.api.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = null;

        Code.SocialLoginType socialLoginType = null;

        if (provider.equals("google")) {
            log.info("구글 로그인");
            socialLoginType = Code.SocialLoginType.GOOGLE;
            oAuth2UserInfo = new GoogleUserDetails(oAuth2User.getAttributes());
        } else if (provider.equals("kakao")) {
            log.info("카카오 로그인");
            socialLoginType = Code.SocialLoginType.KAKAO;
            oAuth2UserInfo = new KakaoUserDetails(oAuth2User.getAttributes());
        }
        String providerId = oAuth2UserInfo.getProviderId();
        String email = oAuth2UserInfo.getEmail();
        String name = oAuth2UserInfo.getName();

        log.info("email = " + oAuth2UserInfo.getEmail());
        Optional<User> findUser = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        User user;

        if (findUser.isEmpty()) {
            user = User.builder()
                    .userId(provider + providerId)
                    .email(email)
                    .business(Code.YN.N)
                    .userName(name)
                    .provider(socialLoginType.toString())
                    .providerId(providerId)
                    .role(Code.Role.USER)
                    .build();
            userRepository.save(user);
            log.info("신규 사용자 저장: {}", user.getEmail());
        } else {
            user = findUser.get();
            log.info("기존 사용자 로그인: {}", user.getEmail());
        }

        return new CustomOauth2UserDetails(user, oAuth2User.getAttributes());
    }
}
