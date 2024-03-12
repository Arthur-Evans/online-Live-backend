package com.niit.onlivestream.util;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Date;
import java.util.Map;

public class JwtUtil {


    private static final String key = "arthur";

    private static final String claimName = "user";

    public static String getToken(Map<String, Object> claims){
        return JWT.create()
                .withClaim(claimName,claims)
                .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))
                .sign(Algorithm.HMAC256(key));

    }

    public static Map<String, Object> parseToken(String token){
        return JWT.require(Algorithm.HMAC256(key))
                .build()
                .verify(token)
                .getClaim(claimName)
                .asMap();
    }


}
