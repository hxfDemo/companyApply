package com.apply.ism.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.apply.ism.common.Result;
import com.apply.ism.entity.Users;
import com.apply.ism.service.IUsersService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * token拦截验证
 *
 */
@Slf4j
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private IUsersService iUsersService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)throws Exception{
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        response.setCharacterEncoding("utf-8");
        String token = request.getHeader("token");
        String message = "token verify fail";
        int code=5000;
        if(token != null){
            int result = TokenUtil.verify(token);
            if(result ==0 ){
                System.out.println("通过拦截器");
                return true;
            }else if (result == 1){
                message ="登录已失效，请重新登录！";
                code=1000;
            }
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("message","success");
            jsonObject.put("code",200);
            jsonObject.put("data", Result.error(code,message));
            response.getWriter().append(jsonObject.toJSONString());
            System.out.println("认证失败，未通过拦截器："+message);
        }catch (Exception e){
            e.printStackTrace();
            response.sendError(500);
            return false;
        }
        return false;
    }
}
