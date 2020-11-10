package com.apply.ism.common;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Api响应对象
 */

public class Result extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = -1052650877920800810L;

    private Result() {
    }

    public static Result ok() {
        return new Result().code(0);
    }

    public static Result ok(String msg) {
        return new Result().code(0).msg(msg);
    }

    public static Result ok(Map<String, Object> data) {
        return new Result().code(0).data(data);
    }

    public static Result ok(List<Map<String, Object>> datas) {
        return new Result().code(0).datas(datas);
    }

    public static Result ok(Map<String, Object> data, List<Map<String, Object>> datas) {
        return new Result().code(0).data(data).datas(datas);
    }

    public static Result ok(String msg, Map<String, Object> data) {
        return new Result().code(0).msg(msg).data(data);
    }

    public static Result ok(String msg, List<Map<String, Object>> datas) {
        return new Result().code(0).msg(msg).datas(datas);
    }

    public static Result ok(String msg, Map<String, Object> data, List<Map<String, Object>> datas) {
        return new Result().code(0).msg(msg).data(data).datas(datas);
    }

    public static Result error(int code) {
        return new Result().code(code);
    }

    public static Result error(int code, String msg) {
        return new Result().code(code).msg(msg);
    }


    public static Result fail(int err) {
        return new Result().code(err);
    }

    public static Result fail(int err, String msg) {
        return new Result().code(err).msg(msg);
    }

    private Result code(Integer code) {
        this.put("code", code);
        return this;
    }

    private Result msg(String msg) {
        this.put("message", msg);
        return this;
    }

    private Result data(Map<String, Object> data) {
        this.put("data", data);
        return this;
    }

    private Result datas(List<Map<String, Object>> datas) {
        this.put("datas", datas);
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this).toString();
    }
}
