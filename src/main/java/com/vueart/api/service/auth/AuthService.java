package com.vueart.api.service.auth;

import com.vueart.api.dto.request.user.SignInRequest;
import com.vueart.api.dto.request.user.SignUpRequest;
import com.vueart.api.dto.response.auth.TokenDto;
import jakarta.validation.Valid;

public interface AuthService {
    void signUp(@Valid SignUpRequest req);

    TokenDto signIn(@Valid SignInRequest req);
}
