package com.apply.ism.common;

import lombok.Data;

@Data
public class BaseException extends RuntimeException {
    private String message;
    private Long code;

    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String message, Long code) {
        this.message = message;
        this.code = code;
    }

    protected Long getErrorCode(Enum errorEnum) {
        String enumString = errorEnum.toString();
        return Long.parseLong(enumString.split("_")[0]);
    }

    protected String getErrorMessage(Enum errorEnum) {
        String enumString = errorEnum.toString();
        return enumString.split("_")[1];
    }
}
