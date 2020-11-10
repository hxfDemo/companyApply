package com.apply.ism.common;

import lombok.Data;

@Data
public class ServiceException extends BaseException {

    private Long code;
    private String message;

    private ServiceException() {
        super();
    }

    public ServiceException(Long code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public ServiceException(Enum errorEnum) {
        Long code = getErrorCode(errorEnum);
        String message = getErrorMessage(errorEnum);
        this.code = code;
        this.message = message;
    }
}
