package com.vueart.api.service.user;

import com.vueart.api.dto.request.user.UpdatePasswordRequest;
import jakarta.validation.Valid;

public interface UserService {
    void updatePassword(@Valid UpdatePasswordRequest request);
}
