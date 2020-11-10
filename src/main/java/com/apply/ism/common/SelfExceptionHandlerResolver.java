package com.apply.ism.common;

import com.apply.ism.common.error.CommonErrorCodeEnum;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用错误处理器.
 *
 * @author Leon
 * @date 2019-07-29
 */
@ControllerAdvice
@Log4j2
public class SelfExceptionHandlerResolver extends AbstractErrorController {

    @Value("${server.error.path}")
    private String ERROR_PATH;
    private ErrorAttributes errorAttributes;
    @Value("${server.error.response-json-include-trace}")
    private boolean includeTrace = true;
    private String requestStatusCode = "javax.servlet.error.status_code";

    public SelfExceptionHandlerResolver(ErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping
    public ModelAndView handleApplicationJsonError(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> modal = null;
        if ((int) request.getAttribute(requestStatusCode) == HttpStatus.NOT_FOUND.value()) {
            modal = new HashMap<>(8, 0.99F);
            modal.put("message", CommonErrorCodeEnum.ERROR_10008.message + ":" + getErrorAttributes(request, true).get("path"));
            modal.put("code", CommonErrorCodeEnum.ERROR_10008.code);
        }
        return jsonResponseData(request, modal, getException(request));
    }

    /**
     * 处理 content-type:text/html, text/plain application/x-www-form-urlencoded 请求的异常
     * 返回spring-boot默认error页面
     */
    @RequestMapping(produces = {MediaType.TEXT_HTML_VALUE, MediaType.TEXT_PLAIN_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public ModelAndView handleHtmlError(HttpServletRequest request, HttpServletResponse response) {
        Exception exception = this.getException(request);
        HttpStatus status = this.getStatus(request, exception);
        Map<String, Object> model = Collections.unmodifiableMap(super.getErrorAttributes(request, true));
        response.setStatus(status.value());
        ModelAndView modelAndView = this.resolveErrorView(request, response, status, model);
        modelAndView = modelAndView == null ? new ModelAndView("error", model) : modelAndView;
        if ((int) request.getAttribute(requestStatusCode) == HttpStatus.NOT_FOUND.value()) {
            modelAndView.addObject("message", "访问的资源不存在");
        }
        return modelAndView;
    }


    /**
     * 500错误.
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView serverError(HttpServletRequest request, Exception ex) {
        HashMap<String, Object> model = new HashMap<>(8, 0.99F);
        model.put("code", CommonErrorCodeEnum.ERROR_10005.code);
        model.put("message", CommonErrorCodeEnum.ERROR_10005.message);
        return jsonResponseData(request, model, ex);
    }

    @ExceptionHandler(ServiceException.class)
    public ModelAndView serviceError(HttpServletRequest request, Exception ex) {
        Map<String, Object> model = new HashMap<>(16, 0.99F);
        model.put("code", ((ServiceException) ex).getCode());
        return jsonResponseData(request, model, ex);
    }

    @ExceptionHandler(SystemException.class)
    public ModelAndView systemError(HttpServletRequest request, Exception ex) {
        Map<String, Object> model = new HashMap<>(16, 0.99F);
        model.put("code", ((SystemException) ex).getCode());
        return jsonResponseData(request, model, ex);
    }

    /**
     * 处理简单参数验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView constraintViolationExceptionHandler(HttpServletRequest request,
                                                            ConstraintViolationException violationException) {
        Map<String, Object> errorParam = new HashMap<>(16, 0.99F);
        errorParam.put("code", CommonErrorCodeEnum.ERROR_10002.code);
        return jsonResponseData(request, errorParam, violationException);
    }

    /**
     * 请求参数不是标准JSON格式异常
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ModelAndView httpMessageNotReadableException(HttpServletRequest request, Exception ex) throws Exception {
        Map<String, Object> model = new HashMap<>(16, 0.99F);
        if (ex.getMessage().contains("java.util.ArrayList")) {
            model.put("message", CommonErrorCodeEnum.ERROR_10007.message);
            model.put("code", CommonErrorCodeEnum.ERROR_10007.code);
        } else {
            model.put("code", CommonErrorCodeEnum.ERROR_10004.code);
            model.put("message", CommonErrorCodeEnum.ERROR_10004.message);
        }
        return jsonResponseData(request, model, ex);
    }

    /**
     * 处理复杂参数验证异常
     */
    @ExceptionHandler({BindException.class})
    public ModelAndView bindException(HttpServletRequest request, HttpServletResponse response, BindException ex) throws Exception {
        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        Map<String, Object> errorParam = new HashMap<>(16, 0.99F);

        StringBuilder message = new StringBuilder();
        fieldErrors.stream().forEach(fieldError -> message.append(fieldError.getField())
                .append(": ").append(fieldError.getDefaultMessage()).append(", "));
        errorParam.put("code", CommonErrorCodeEnum.ERROR_10002.code);
        errorParam.put("message", message.substring(0, message.length() - 2));
        return jsonResponseData(request, errorParam, ex);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ModelAndView methodArgumentNotValidException(HttpServletRequest request, HttpServletResponse response, MethodArgumentNotValidException ex) throws Exception {
        return bindException(request, response, new BindException(ex.getBindingResult()));
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ModelAndView methodNotSupportException(HttpServletRequest request, HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> model = new HashMap<>(8, 0.99F);
        model.put("code", CommonErrorCodeEnum.ERROR_10006.code);
        return jsonResponseData(request, model, ex);
    }


    private ModelAndView jsonResponseData(HttpServletRequest request, Map<String, Object> responseData, Exception exception) {
        Map<String, Object> model = super.getErrorAttributes(request, true);
        log.error("error trace: {}", model.get("trace"));
        if (!includeTrace) {
            model.remove("trace");
        }
        model.remove("status");
        model.remove("error");
        model.remove("errors");
        model.remove("path");
        model.put("timestamp", LocalDateTime.now().toString());

        if (responseData != null) {
            model.putAll(responseData);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("code",200);
        map.put("message","success");
        map.put("data",model);
        ModelAndView jsonView = new ModelAndView();
        jsonView.setView(new MappingJackson2JsonView());
        jsonView.setStatus(HttpStatus.OK);
        jsonView.addAllObjects(map);
        return jsonView;
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }


    private <T extends Throwable> T getException(HttpServletRequest request) {
        T exception = (T) this.errorAttributes.getError(new ServletWebRequest(request));
        if (exception != null) {
            return exception;
        }
        return null;
    }

    public HttpStatus getStatus(HttpServletRequest request, Exception exception) {
        HttpStatus httpStatus = super.getStatus(request);
        return httpStatus;
    }

}
