/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.controllers;

import com.encooked.services.UserService;
import com.encooked.components.JwtTokenUtil;
import com.encooked.dto.ErrorResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @version 1.0
 * @author obinna.asuzu
 */
@RestController
@RequestMapping("/oauth/v1")
@Api(value = "Authentication Api", description = "REST API for Authentication", tags = { "Oauth" })
public class AuthenticationController {
    
    @Autowired
    UserService userService;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Error message")
    public void handleError() {
    }
    
    
    @ApiOperation(value = "Returns users token on successful authentication")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = String.class, message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @ApiParam(value = "", type = "header", required = true, name = "Authorization", examples = @Example(value = {
        @ExampleProperty(value = "Basic dXNlcjpwYXNzd29yZA==")
    }))
    @RequestMapping(name = "/request-token", method = RequestMethod.GET)
    public ResponseEntity<String> login(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        String doGenerateToken = jwtTokenUtil.doGenerateToken(user);
        return ResponseEntity.ok(doGenerateToken);
    }
}
