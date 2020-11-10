package com.apply.ism.common;

import lombok.Data;

@Data
public class SystemException extends BaseException {
    private String message;
    private Long code;

    private SystemException() {
        super();
    }

    public SystemException(Long code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public SystemException(Enum errorEnum) {
        Long errorCode = getErrorCode(errorEnum);
        String errorMessage = getErrorMessage(errorEnum);
        this.code = errorCode;
        this.message = errorMessage;
    }


}
