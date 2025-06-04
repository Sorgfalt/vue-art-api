package com.vueart.api.service.user;

import com.vueart.api.common.util.AES256Util;
import com.vueart.api.common.util.BusinessRegisterApiUtil;
import com.vueart.api.core.enums.Code;
import com.vueart.api.core.exception.VueArtApiException;
import com.vueart.api.dto.request.business.BusinessRegisterListDto;
import com.vueart.api.dto.request.user.UpdatePasswordRequest;
import com.vueart.api.entity.User;
import com.vueart.api.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AES256Util aes256Util;
    private final PasswordEncoder passwordEncoder;
    private final BusinessRegisterApiUtil businessRegisterApiUtil;

    @Override
    @Transactional
    public void updatePassword(UpdatePasswordRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new VueArtApiException(Code.ErrorCode.NOT_FOUND_USER_ID));

        String currentPassword = aes256Util.decode(request.currentPassword());
        String newPassword = aes256Util.decode(request.newPassword());
        String confirmPassword = aes256Util.decode(request.confirmPassword());

        log.info("getId: {}", user.getId());
        log.info("user.getPassword() => {}", aes256Util.decode(user.getPassword()));
        log.info("Current password is => {}", currentPassword);
        log.info("New password is => {}", newPassword);
        log.info("Confirm password is => {}", confirmPassword);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new VueArtApiException(Code.ErrorCode.CURRENT_PASSWORD_INCORRECT);
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new VueArtApiException(Code.ErrorCode.NEW_PASSWORD_SAME_AS_OLD);
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new VueArtApiException(Code.ErrorCode.PASSWORD_CONFIRMATION_MISMATCH);
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        User updated = user.updatePassword(request.userId(), encodedNewPassword);
        userRepository.save(updated);
    }

    @Override
    public boolean isBusinessRegistered(BusinessRegisterListDto req) {
        return businessRegisterApiUtil.getBusinessRegisterCheck(req);
    }
}
