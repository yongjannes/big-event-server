package com.jy.bigevent;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTest {
    @Test
    public void setgen(){
        Map<String,Object> claims = new HashMap<>();
        claims.put("id",1);
        claims.put("username","admin");


        //生成JJwt
        String token = JWT.create()
                .withClaim("user",claims)//添加载荷
                .withExpiresAt(new Date(System.currentTimeMillis()+60*1000*60))//添加过期时间
                .sign(Algorithm.HMAC256("yong"));//指定算法  配置密钥

        System.out.println(token);
    }

    @Test
    public  void testParse(){
        //定义字符串模拟用户传过来的token
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9" +
                ".eyJleHAiOjE3NDI4MDUwNDMsInVzZXIiOnsiaWQiOjEsInVzZXJuYW1lIjoiYWRtaW4ifX0" +
                ".jJCBmRSl7F9pdaWxZOA8O8PmSwStE_NY49WRczi3imU";

        JWTVerifier verifier = JWT.require(Algorithm.HMAC256("yong")).build();
        DecodedJWT jwt = verifier.verify(token);//验证token 生成一个解析后的JWT对象
        Map<String, Claim> claims = jwt.getClaims(); //获取载荷信息
        System.out.println(claims.get("user").asMap());
        //如果篡改了头部和载荷部分的数据，那么验证失败
        //如果秘钥改了，验证失败
        //token过期
    }
}
