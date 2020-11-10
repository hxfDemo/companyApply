package com.apply.ism.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * 微服务返回对象包装类
 * @author Leon
 */
@Data
public class Message implements Serializable {
    private static final long serialVersionUID = 2466437063135960905L;

    @JsonIgnore
    public final static int OK = 200;
    @JsonIgnore
    public final static int ERROR = 500;

    private int code = OK;
    private String message = "success";
    private Object data = null;

    public Message() {
    }

    public Message(int code, Map<String, Object> data) {
        this.code = code;
        this.data = data;
    }

    public Message(int code, String message, Map<String, Object> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Message success() {
        return new Message(OK, new LinkedHashMap<>());
    }

    public static Message success(Map<String, Object> data) {
        return new Message(OK, data);
    }

    public void setCode(int code) {
        this.code = code;
        if (code != OK) {
            if (StringUtils.isBlank(message) || "success".equals(message)) {
                message = "something was wrong !";
            }
        }
    }

    public Object getData() {
        if (data == null) {
            return new LinkedHashMap<>();
        }
        return data;
    }

    public void addData(String key, Object data) {
        if (this.data == null) {
            this.data = new LinkedHashMap<String, Object>();
        } else if (!(this.data instanceof Map)) {
            throw new RuntimeException("illegal argument");
        }
        ((LinkedHashMap<String, Object>) this.data).put(key, data);
    }

    public void addData(Map<String, Object> map) {
        if (map == null) {
            return;
        } else if (this.data == null) {
            data = new LinkedHashMap<>();
        }
        ((LinkedHashMap<String, Object>) data).putAll(map);
    }

    public void addData(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof Map) {
            addData((Map<String, Object>) object);
        } else if (object instanceof Collection) {
            data = object;
        } else if (object.getClass().isArray()) {
            data = object;
        } else if (object instanceof String) {
            throw new RuntimeException("不能是字符串");
        } else if (object instanceof Integer) {
            throw new RuntimeException("不能是Integer");
        } else if (object instanceof Long) {
            throw new RuntimeException("不能是Long");
        } else if (object instanceof Double) {
            throw new RuntimeException("不能是Double");
        } else if (object instanceof Float) {
            throw new RuntimeException("不能是Float");
        } else {
            data = object;
        }
    }

    public void removeData(String key) {
        if (StringUtils.isNotEmpty(key)) {
            ((LinkedHashMap<String, Object>) data).remove(key);
        }
    }

}