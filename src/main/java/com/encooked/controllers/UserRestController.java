/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.controllers;

import com.encooked.components.JwtTokenUtil;
import com.encooked.components.MessageComponent;
import com.encooked.dto.UserDto;
import com.encooked.entities.UserEntity;
import com.encooked.dto.ErrorResponse;
import com.encooked.dto.Resource;
import com.encooked.exceptions.ServiceException;
import com.encooked.services.UserService;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author obinna.asuzu
 */
@RestController
@RequestMapping("/api/v1/users")
@Api(value = "User API", description = "User Rest API", authorizations = {
    @Authorization("USER_READ")}, tags = {"User"})
public class UserRestController {

    @Autowired
    UserService userService;
    @Autowired
    MessageComponent messageComponent;

    Authentication authentication;
    String link;

    @Autowired
    private EurekaClient discoveryClient;
    
    private org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    @ApiOperation(value = "Return list of users")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDto.class, responseContainer = "List", message = "")
    })
    @GetMapping()
    public ResponseEntity list(HttpServletRequest request) {
        initContext(request);

        List<UserDto> users = userService
                .getAllUsers().stream()
                .map(u -> toDto(u))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @ApiOperation(value = "Return list of users paginated")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDto.class, responseContainer = "List", message = "")
    })
    @GetMapping("/pagination")
    public ResponseEntity list(HttpServletRequest request, @RequestParam int page, @RequestParam int size) {
        initContext(request);
        Page<UserEntity> users = userService.getAllUsers(page, size);
        Resource<List<UserDto>> resource = new Resource<>(users.getContent().stream()
                .map(u -> toDto(u))
                .collect(Collectors.toList()));

        if (users.hasNext()) {
            Pageable pageable = users.nextPageable();
            resource.add(new Link(link + "api/v1/users/pagination?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(), "next"));
        }

        if (users.hasPrevious()) {
            Pageable pageable = users.previousPageable();
            resource.add(new Link(link + "api/v1/users/pagination?page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize(), "previous"));
        }

        return ResponseEntity.ok(resource);
    }

    @ApiOperation(value = "get user details by username or \"me\" as id for logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = UserDto.class, responseContainer = "List", message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable String id) throws ServiceException {
        return ResponseEntity.ok(new UserDto(userService.getUser(resolve(id))));
    }

    @ApiOperation(value = "get user profile infomations by username or \"me\" as id for logged in user")
    @ApiResponses(value = {
        @ApiResponse(code = 200, response = Object.class, responseContainer = "List", message = "OK"),
        @ApiResponse(code = 404, response = ErrorResponse.class, message = "NOT FOUND")
    })
    @GetMapping("/{id}/profile")
    public ResponseEntity getProfile(@PathVariable String id) throws ServiceException {
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
    public ResponseEntity put(@PathVariable String id, @RequestBody Map<String, String> principles) throws ServiceException {
        UserEntity user = userService.updateUserProfile(resolve(id), principles);
        return ResponseEntity.accepted().body(new UserDto(user));
    }

    @ApiOperation(value = "create new user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = UserDto.class, message = "ACCEPTED"),
        @ApiResponse(code = 400, response = ErrorResponse.class, message = "BAD REQUEST"),
        @ApiResponse(code = 500, response = ErrorResponse.class, message = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/create")
    public ResponseEntity post(@RequestBody UserDto user) throws ServiceException {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getPrinciples().get("firstname");
        String lastname = user.getPrinciples().get("lastname");
        String email = user.getPrinciples().get("email");
        String phone = user.getPrinciples().get("phone");
        List<String> roles = user.getRoles();

        UserEntity createdUser = userService.createUser(username, password, firstname, lastname, email, phone, roles);
        return ResponseEntity.accepted().body(new UserDto(createdUser));
    }

    @ApiOperation(value = "create new user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = UserDto.class, message = "ACCEPTED"),
        @ApiResponse(code = 400, response = ErrorResponse.class, message = "BAD REQUEST"),
        @ApiResponse(code = 500, response = ErrorResponse.class, message = "INTERNAL SERVER ERROR")
    })
    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody UserDto user) throws Exception {
        String username = user.getUsername();
        String password = user.getPassword();
        String firstname = user.getPrinciples().get("firstname");
        String lastname = user.getPrinciples().get("lastname");
        String email = user.getPrinciples().get("email");
        String phone = user.getPrinciples().get("phone");
        List<String> roles = user.getRoles();

        UserEntity createdUser = userService.registerUser(username, password, firstname, lastname, email, phone, roles);
        return ResponseEntity.accepted().body(new UserDto(createdUser));
    }

    @ApiOperation(value = "delete user")
    @ApiResponses(value = {
        @ApiResponse(code = 202, response = Map.class, message = "ACCEPTED"),
        @ApiResponse(code = 403, response = ErrorResponse.class, message = "FORBIDDEN"),
        @ApiResponse(code = 500, response = Boolean.class, message = "INTERNAL SERVER ERROR")
    })
    @DeleteMapping("/{id}/")
    public ResponseEntity delete(@PathVariable String id) throws Exception {
        if (id.equalsIgnoreCase("me")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("You cannot delete your account"));
        } else {
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(userService.deactivateUser(resolve(id)));
        }
    }

    private String resolve(String id) {
        if (id.equalsIgnoreCase("me")) {
            authentication = authentication == null
                    ? SecurityContextHolder.getContext().getAuthentication()
                    : authentication;
            return authentication.getName();
        }
        return id;
    }

    private void initContext(HttpServletRequest request) {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("-----------------------------------..>");
        authentication.getAuthorities().forEach(a -> log.info(a.getAuthority()));
        String path = discoveryClient.getNextServerFromEureka("GATEWAY", false).getHomePageUrl();
        try {
            URL url = new URL(path);
            path = url.getProtocol() + "://" + url.getHost() + "/";
        } catch (MalformedURLException ex) {
        }
        link = path + request.getHeader("context-path") + "/";
    }

    private boolean userHasAuthority(String authority) {
        return authentication.getAuthorities()
                .stream()
                .anyMatch((grantedAuthority) -> authority.equals(grantedAuthority.getAuthority()));
    }

    private UserDto toDto(UserEntity u) {
        UserDto user = new UserDto(u.getUsername());
        if (userHasAuthority("ADMIN.USER_WRITE") && !u.isSystem()) {
            user.add(new Link(link + "api/v1/users/" + u.getUsername() + "/", "delete"));
        }
        user.add(new Link(link + "api/v1/users/" + u.getUsername(), "details"));
        user.add(new Link(link + "api/v1/users/" + u.getUsername() + "/profile", "profile"));
        return user;
    }

}
