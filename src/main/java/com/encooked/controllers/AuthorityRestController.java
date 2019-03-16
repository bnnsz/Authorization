/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.controllers;

import com.encooked.dto.ErrorResponse;
import com.encooked.dto.UserDto;
import com.encooked.enums.AuthorityType;
import com.encooked.exceptions.RecordNotFoundException;
import com.encooked.services.AuthorityService;
import com.encooked.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author obinna.asuzu
 */
@RestController
@RequestMapping("/api/v1/authorities")
@Api(value = "Roles and Permissions API", description = "Roles and Permissions Rest API")
public class AuthorityRestController {

    @Autowired
    UserService userService;

    @Autowired
    AuthorityService authorityService;

    @ApiOperation(value = "create new authority")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = Boolean.class, message = "ACCEPTED"),
        @ApiResponse(code = 400, response = ErrorResponse.class, message = "BAD REQUEST"),
        @ApiResponse(code = 500, response = ErrorResponse.class, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/{type}/create")
    public ResponseEntity create(@PathVariable AuthorityType type, @RequestParam String value) throws RecordNotFoundException, Exception {
        switch (type) {
            case GRANT:
                authorityService.createAuthority(() -> value);
                return ResponseEntity.accepted().body(true);
            case PRIVILEDGE:
                authorityService.createPriviledge(value);
                return ResponseEntity.accepted().body(true);
            case ROLE:
                authorityService.createRole(value);
                return ResponseEntity.accepted().body(true);
        }
        throw new Exception("Invalid type");
    }

    @ApiOperation(value = "get list of all authorities by type")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = UserDto.class, message = "ACCEPTED"),
        @ApiResponse(code = 400, response = ErrorResponse.class, message = "BAD REQUEST"),
        @ApiResponse(code = 500, response = ErrorResponse.class, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("/{type}")
    public ResponseEntity get(@PathVariable AuthorityType type) throws RecordNotFoundException, Exception {
        switch (type) {
            case GRANT:
                List<String> authorities = authorityService.getAllAuthorities();
                return ResponseEntity.accepted().body(authorities);
            case PRIVILEDGE:
                List<String> permissions = authorityService.getAllPermission();
                return ResponseEntity.accepted().body(permissions);
            case ROLE:
                List<String> roles = authorityService.getAllRoles();
                return ResponseEntity.accepted().body(roles);
        }
        throw new Exception("Invalid type");
    }

    @ApiOperation(value = "delete authority by type")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = UserDto.class, message = "ACCEPTED"),
        @ApiResponse(code = 400, response = ErrorResponse.class, message = "BAD REQUEST"),
        @ApiResponse(code = 500, response = ErrorResponse.class, message = "INTERNAL SERVER ERROR")
    })
    @GetMapping("{type}/delete")
    public ResponseEntity remove(@PathVariable AuthorityType type, @RequestParam String value) throws RecordNotFoundException, Exception {
        switch (type) {
            case GRANT:
                authorityService.removeAuthority(() -> value);
                return ResponseEntity.accepted().body(1);
            case PRIVILEDGE:
                return ResponseEntity.accepted().body(authorityService.deletePriviledge(value));

            case ROLE:
                return ResponseEntity.accepted().body(authorityService.deleteRole(value));
        }
        throw new Exception("Invalid type");
    }
}
