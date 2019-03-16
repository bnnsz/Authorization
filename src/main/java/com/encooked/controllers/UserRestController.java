/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.controllers;

import com.encooked.dto.UserDto;
import com.encooked.entities.UserEntity;
import com.encooked.dto.ErrorResponse;
import com.encooked.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author obinna.asuzu
 */
@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User API",description = "User Rest API", authorizations = {@Authorization("USER_READ")})
public class UserRestController {

    @Autowired
    UserService userService;

    @ApiOperation(value = "Return list of users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDto.class, responseContainer = "List", message = "")
    })
    @GetMapping()
    public ResponseEntity list() {
        List<UserDto> users = userService
                .getAllUsers().stream()
                .map(u -> new UserDto(u))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "get user details by username or \"me\" as id for logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDto.class, responseContainer = "List", message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(resolve(id)));
    }

    @ApiOperation(value = "get user profile infomations by username or \"me\" as id for logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = Object.class, responseContainer = "List", message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @GetMapping("/{id}/profile")
    public ResponseEntity getProfile(@PathVariable String id) {
        if (id.equalsIgnoreCase("me")) {

        }
        return ResponseEntity.ok(userService.getUserProfile(resolve(id)));
    }
    
    @ApiOperation(value = "get user profile infomations by username or \"me\" as id for logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = Map.class, message = "ACCEPTED"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @PutMapping("/{id}/update")
    public ResponseEntity put(@PathVariable String id, @RequestBody Map<String, String> principles) {
        UserEntity user = userService.updateUserProfile(resolve(id), principles);
        return ResponseEntity.accepted().body(new UserDto(user));
    }
    
    @ApiOperation(value = "change password for the specified username or \"me\" as id for logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = Boolean.class, message = "ACCEPTED"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @PostMapping("/{id}/changePassword")
    public ResponseEntity changePassword(
            @PathVariable String id, 
            @RequestBody String oldPassword, 
            @RequestBody String newPassword) {
        boolean changed = userService.changePassword(resolve(id), oldPassword,newPassword);
        return ResponseEntity.accepted().body(changed);
    }

    @ApiOperation(value = "create new user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = UserDto.class, message = "ACCEPTED"),
        @ApiResponse(code = 400, response = ErrorResponse.class, message = "BAD REQUEST"),
        @ApiResponse(code = 500, response = ErrorResponse.class, message = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/create")
    public ResponseEntity post(@RequestBody UserDto user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getPrinciples().get("firstname");
        String lastname = user.getPrinciples().get("lastname");
        String email = user.getPrinciples().get("email");
        String phone = user.getPrinciples().get("phone");

        UserEntity createdUser = userService.createUser(username, password, firstname, lastname, email, phone);
        return ResponseEntity.accepted().body(new UserDto(createdUser));
    }

    @ApiOperation(value = "delete user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = Map.class, message = "ACCEPTED"),
        @ApiResponse(code = 403, response = ErrorResponse.class, message = "FORBIDDEN"),
        @ApiResponse(code = 500, response = Boolean.class, message = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/{id}/")
    public ResponseEntity delete(@PathVariable String id) {
        if (id.equalsIgnoreCase("me")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("You cannot delete your account"));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.deactivateUser(resolve(id)));
        }
    }

    private String resolve(String id) {
        if (id.equalsIgnoreCase("me")) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            return authentication.getName();
        }
        return id;
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public void handleError() {
    }

}
