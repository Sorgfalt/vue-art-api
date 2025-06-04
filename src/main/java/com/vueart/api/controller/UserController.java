package com.vueart.api.controller;

import com.vueart.api.common.response.CommonApiResponse;
import com.vueart.api.common.response.SuccessResponse;
import com.vueart.api.core.enums.Code;
import com.vueart.api.dto.request.business.BusinessRegisterListDto;
import com.vueart.api.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "유저",
            description = "사업자 번호"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success",
                    content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "400", description = "입력 값이 잘못 되었습니다."),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/business-register-check")
    public CommonApiResponse<Boolean> isBusinessRegistered(@RequestBody BusinessRegisterListDto req) {
        boolean bool = userService.isBusinessRegistered(req);
        return new CommonApiResponse<>(HttpStatus.OK.value(), Code.ApiResponseCode.SUCCESS.getCode(), bool);
    }
}
