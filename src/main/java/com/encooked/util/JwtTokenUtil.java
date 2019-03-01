/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.util;

import com.encooked.dto.UserDto;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author obinna.asuzu
 */
@Component
public class JwtTokenUtil implements Serializable {

    @Value("${jwt.security.key}")
    private String jwtKey;
    @Value("${security.access-token-validity}")
    private long accessTokenValidity;

    Base64 encoder;
    Gson gson;
    
    public void init(){
        encoder = new Base64();
        gson = new Gson();
    }
    

    public String doGenerateToken(UserDetails userDetails) {
        String userData = encoder.encodeToString(gson.toJson(new UserDto(userDetails)).getBytes());
        Claims claims = Jwts.claims().setSubject(userData);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://encooked.com")
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenValidity * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }
    // Other methods

    public boolean validateToken(String authToken, UserDetails userDetails) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(authToken)
                .getBody();
        
        if(claims == null || claims.isEmpty() || claims.getExpiration().before(new Date(System.currentTimeMillis()))){
            return false;
        }
        
        UserDto user = gson.fromJson(new String(encoder.decode(claims.getSubject())), UserDto.class);
        return user.validate(new UserDto(userDetails));
    }

    public String getUsernameFromToken(String authToken) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(authToken)
                .getBody().get("username", String.class);
    }
    
    
    
    
}
