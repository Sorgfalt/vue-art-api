package com.vueart.api.common.response;

import lombok.Getter;

@Getter
public class CommonApiResponse<T> {
    private final Integer status;
    private final Object message;
    private final T data;

    public CommonApiResponse(Integer status, Object message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
