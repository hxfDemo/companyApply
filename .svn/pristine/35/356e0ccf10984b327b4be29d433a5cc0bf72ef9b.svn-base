package com.apply.ism.common.utils;

import com.apply.ism.entity.Users;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.bouncycastle.util.encoders.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Date;

/**
 * 生成token
 */

@Slf4j
public class TokenUtil {
    private static final long EXPIRE_TIME = 12*60*60*1000;
    public static final String SECRET = DigestUtils.md5Hex("company-apply");
    public static String tokens = null;

    /**
     * 签名生成
     * @param user
     * @return
     */
    public static String sign(Users user){
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + EXPIRE_TIME);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("userid", user.getId().toString())
                    .withClaim("role", user.getRole().toString())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    .sign(Algorithm.HMAC256(SECRET));
        } catch (Exception e){
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     * @param token
     * @return
     */
    public static int verify(String token){
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).withIssuer("auth0").build();
            DecodedJWT jwt = verifier.verify(token);
            System.out.println("认证通过：");
//            System.out.println("userid: " + jwt.getClaim("userid").asString());
            System.out.println("过期时间：      " + jwt.getExpiresAt());
            tokens = token;
            return 0;
        } catch (Exception e){
//            log.error("token鉴权："+e.getMessage());
            if (e.getMessage().contains("has expired on")){
//                log.error("登录已过期！");
                return 1;
            }
            return 2;
        }
    }

    /**
     * 获取用户id
     * @return
     */
    public static Long getUserId(){
        Long userid=null;
        try {
            JWT decode = JWT.decode(tokens);
            userid=Long.valueOf(decode.getClaim("userid").asString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return userid;
    }
}
