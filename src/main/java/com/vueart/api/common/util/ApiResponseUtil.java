package com.vueart.api.common.util;

import com.vueart.api.common.response.CommonApiResponse;
import org.springframework.stereotype.Component;

import static com.vueart.api.core.enums.Code.ApiResponseCode.SUCCESS;

@Component
public class ApiResponseUtil {
    public static <T> CommonApiResponse<T> success(T data) {
        return new CommonApiResponse<T>(SUCCESS.getStatus(), SUCCESS.getMessage(), data);
    }
}
