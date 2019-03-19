/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.controllers;

import com.encooked.services.UserService;
import com.encooked.components.JwtTokenUtil;
import com.encooked.dto.ErrorResponse;
import com.encooked.dto.UserDto;
import com.encooked.exceptions.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @version 1.0
 * @author obinna.asuzu
 */
@RestController
@RequestMapping("/oauth/v1")
@Api(value = "Authentication Api", description = "REST API for Authentication", tags = {"Oauth"})
public class AuthenticationController {

    @Autowired
    UserService userService;

   

    @ApiOperation(value = "Returns users token on successful authentication")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = String.class, message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @ApiParam(value = "", type = "header", required = true, name = "Authorization", examples = @Example(value = {
        @ExampleProperty(value = "Basic dXNlcjpwYXNzd29yZA==")
    }))
    @RequestMapping(value = "/request-token", method = RequestMethod.GET)
    public ResponseEntity<String> login(@RequestHeader String authorization) throws ServiceException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String token = userService.requestToken((String) auth.getPrincipal());
        
        return ResponseEntity.ok(token);
    }

    @ApiOperation(value = "Activates user account and returns a new token")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = String.class, message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public ResponseEntity<String> activate(@RequestParam String token) throws Exception {
        String doGenerateToken = userService.activateUser(token);
        return ResponseEntity.ok(doGenerateToken);
    }
    
    @ApiOperation(value = "Verifies user account using a token")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDto.class, message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public ResponseEntity<UserDto> verify(@RequestParam String token) throws Exception {
        UserDetails user = userService.verifyToken(token);
        return ResponseEntity.ok(new UserDto(user));
    }
}
