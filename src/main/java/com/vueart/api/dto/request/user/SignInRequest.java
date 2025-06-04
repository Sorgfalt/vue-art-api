package com.vueart.api.dto.request.user;

import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @NotNull(message = "userId은 필수입니다.")
        String userId,
        @NotNull(message = "password는 필수입니다.")
        String password) {
}

