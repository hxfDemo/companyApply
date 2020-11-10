package com.apply.ism.common.returnhandler;

import com.alibaba.fastjson.JSONObject;
import com.apply.ism.common.Message;
import com.apply.ism.common.constants.SystemConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Leon
 * @date 2019-07-24
 */
@Log4j2
public class ReturnMessageConvertHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ReturnMessageConvertHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer modelAndViewContainer, NativeWebRequest webRequest) throws Exception {
        Object printObject;
        HttpServletRequest request = ((ServletWebRequest) webRequest).getRequest();
        if (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), IgnoreMessageConvert.class)
                || returnType.hasMethodAnnotation(IgnoreMessageConvert.class)
                || request.getRequestURI().contains("/actuator")) {
            delegate.handleReturnValue(returnValue, returnType, modelAndViewContainer, webRequest);
            printObject = returnValue;
        } else {
            Message returnMessage = Message.success();
            returnMessage.addData(returnValue);
            delegate.handleReturnValue(returnMessage, returnType, modelAndViewContainer, webRequest);
            printObject = returnMessage;
        }
        if (request.getAttribute(SystemConstant.PRINT_RETURN_ATTRIBUTE_NAME) == null
                || ((boolean) request.getAttribute(SystemConstant.PRINT_RETURN_ATTRIBUTE_NAME))) {
            log.info("uri:{},returnValue:{}", request.getRequestURI(), JSONObject.toJSONString(printObject));
        }
    }

}
