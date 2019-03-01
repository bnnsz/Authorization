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
@RequestMapping("/v1/users")
public class UserRestController {

    @Autowired
    UserService userService;

    @GetMapping()
    public ResponseEntity list() {
        List<UserDto> users = userService
                .getAllUsers().stream()
                .map(u -> new UserDto(u))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable String id) {
        
        return ResponseEntity.ok(userService.getUser(resolve(id)));
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity getProfile(@PathVariable String id) {
        if (id.equalsIgnoreCase("me")) {

        }
        return ResponseEntity.ok(userService.getUserProfile(resolve(id)));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity put(@PathVariable String id, @RequestBody Map<String, String> principles) {
        UserEntity user = userService.updateUserProfile(resolve(id), principles);
        return ResponseEntity.accepted().body(new UserDto(user));
    }

    @PostMapping("/create")
    public ResponseEntity post(@RequestBody UserDto user) {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getPrinciples().get("firstname");
        String lastname = user.getPrinciples().get("firstname");
        String email = user.getPrinciples().get("firstname");
        String phone = user.getPrinciples().get("firstname");

        UserEntity createdUser = userService.createUser(username, password, firstname, lastname, email, phone);
        return ResponseEntity.accepted().body(createdUser);
    }

    @DeleteMapping("/{id}/")
    public ResponseEntity delete(@PathVariable String id) {
        if (id.equalsIgnoreCase("me")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("You cannot delete your account"));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.deactivateUser(resolve(id)));
        }
    }
    
    private String resolve(String id){
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
