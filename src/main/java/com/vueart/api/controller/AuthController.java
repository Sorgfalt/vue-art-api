package com.vueart.api.controller;

import com.vueart.api.common.response.CommonApiResponse;
import com.vueart.api.common.response.SuccessResponse;
import com.vueart.api.core.enums.Code;
import com.vueart.api.dto.request.user.SignInRequest;
import com.vueart.api.dto.request.user.SignUpRequest;
import com.vueart.api.dto.response.auth.TokenDto;
import com.vueart.api.service.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "인증 관련 API")
public class AuthController {
    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = "이메일, 비밀번호, 닉네임 정보를 받아 회원가입 처리"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입이 완료되었습니다.",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었습니다."),
            @ApiResponse(responseCode = "409", description = "이미 가입된 이메일입니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/sign-up")
    public SuccessResponse signUp(@Valid @RequestBody SignUpRequest req) {
        authService.signUp(req);
        return new SuccessResponse(Code.ApiResponseCode.SUCCESS_SIGN_UP.getMessage());
    }

    @Operation(summary = "로그인", description = "이메일, 비밀번호 정보를 받아 로그인 처리")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "400", description = "아이디 또는 비밀번호가 일치하지 않습니다."),
            @ApiResponse(responseCode = "404", description = "가입된 회원이 아닙니다. 회원 가입을 먼저 진행해주세요."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/sign-in")
    public CommonApiResponse<TokenDto> signIn(@Valid @RequestBody SignInRequest req) {
        TokenDto accessToken = authService.signIn(req);
        return new CommonApiResponse<>(HttpStatus.OK.value(), Code.ApiResponseCode.SUCCESS.getCode(), accessToken);
    }
}
