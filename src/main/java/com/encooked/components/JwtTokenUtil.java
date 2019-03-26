/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import com.encooked.dto.UserDto;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
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
    
    @PostConstruct
    public void init(){
        encoder = new Base64();
        gson = new Gson();
    }
    

    public String doGenerateToken(UserDetails userDetails) {
        Claims claims = Jwts.claims().setSubject(userDetails.getUsername());
        claims.put("credential", userDetails.getPassword());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuer("http://encooked.com")
                .setIssuedAt(today())
                .setExpiration(configuredDate())
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
    }
    
    private Date today(){
         return new Date(System.currentTimeMillis());
    }
    
    private Date configuredDate(){
         return new Date(System.currentTimeMillis() + accessTokenValidity * 1000);
    }
    
    
    // Other methods
    public String getUsername(String authToken) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(authToken)
                .getBody().getSubject();
    }
    
    // Other methods
    public String getPassword(String authToken) {
        return Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(authToken)
                .getBody().getSubject();
    }
    
    public boolean validateToken(String authToken, UserDetails userDetails) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtKey)
                .parseClaimsJws(authToken)
                .getBody();

        if (claims == null || claims.isEmpty() || claims.getExpiration().before(new Date(System.currentTimeMillis()))) {
            return false;
        }

        String username = claims.getSubject();
        return username.equals(userDetails.getUsername());
    }
    
    
    
    
}
