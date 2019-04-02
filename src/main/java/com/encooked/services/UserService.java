/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.services;

import com.encooked.components.JwtTokenUtil;
import com.encooked.components.MessageComponent;
import com.encooked.dto.UserDto;
import com.encooked.entities.RoleEntity;
import com.encooked.entities.TokenEntity;
import com.encooked.entities.UserEntity;
import com.encooked.enums.Error;
import com.encooked.exceptions.ServiceException;
import com.encooked.repositories.GrantedPrivilegeEntityRepository;
import com.encooked.repositories.PrivilegeEntityRepository;
import com.encooked.repositories.RoleEntityRepository;
import com.encooked.repositories.TokenEntityRepository;
import com.encooked.repositories.UserEntityRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author obinna.asuzu
 */
@Service
public class UserService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    TokenEntityRepository tokenEntityRepository;

    @Autowired
    RoleEntityRepository roleEntityRepository;

    @Autowired
    PrivilegeEntityRepository privilegeEntityRepository;

    @Autowired
    GrantedPrivilegeEntityRepository grantedPrivilegeEntityRepository;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    private MessageComponent messageComponent;

    public String activateUser(String tokenValue) throws Exception, UsernameNotFoundException {
        Optional<TokenEntity> tokenOption = tokenEntityRepository.findByValue(tokenValue);
        if (tokenOption.isPresent()) {
            TokenEntity token = tokenOption.get();
            if (token.isExpired()) {
                throw new ServiceException(Error.token_expired);
            }
            UserEntity user = token.getUser();

            if (user == null) {
                throw new ServiceException(Error.token_invalid);
            }

            if (user.isEnabled()) {
                throw new ServiceException(Error.account_active);
            }

            user.setEnabled(true);
            user.setCredentialsNonExpired(true);
            token.setExpired(true);

            user = userEntityRepository.save(user);

            token.setUser(user);
            tokenEntityRepository.save(token);

            token = new TokenEntity(jwtTokenUtil.doGenerateToken(user), user);
            token = tokenEntityRepository.save(token);
            return token.getValue();
        } else {
            throw new ServiceException(Error.token_invalid);
        }
    }

   
    public UserDetails verifyToken(String value) throws Exception, UsernameNotFoundException {
        TokenEntity token = tokenEntityRepository.findByValue(value)
                .orElseThrow(() -> new ServiceException(Error.token_invalid));
        if (token.isExpired()) {
            throw new ServiceException(Error.token_expired);
        }

        if (token.getUser() == null) {
            throw new ServiceException(Error.token_invalid);
        }

        UserEntity user = token.getUser();

        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid username or password");
        }

        if (!jwtTokenUtil.validateToken(value, user)) {
            throw new ServiceException(Error.token_invalid);
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("Account has expired");
        }

        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account has been locked");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("Account has been disabled");
        }

        return user;
    }

    public UserDetails getUser(String username) throws ServiceException {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));
    }

    @Transactional
    public String requestToken(String username) throws ServiceException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));

        return tokenEntityRepository.findByUserAndExpired(user, false)
                .peek(t -> t.setExpired(true))
                .peek(t -> tokenEntityRepository.save(t))
                .filter(t -> !t.isExpired())
                .findFirst()
                .orElseGet(() -> {
                    TokenEntity token = new TokenEntity(jwtTokenUtil.doGenerateToken(user), user);
                    token.setExpired(false);
                    tokenEntityRepository.save(token);
                    return token;
                }).getValue();
    }

    public Map<String, String> getUserProfile(String username) throws ServiceException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));
        return user.getPrinciples();
    }

    public UserEntity registerUser(
            String username,
            String password,
            String firstname,
            String lastname,
            String email,
            String phone,
            List<String> roles) throws ServiceException {
        UserEntity user = createUser(username, password, firstname, lastname, email, phone, roles);
        if (user != null) {
            int length = 10;
            boolean useLetters = true;
            boolean useNumbers = false;
            String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);
            TokenEntity token = new TokenEntity(generatedString, user);
            token = tokenEntityRepository.save(token);
            messageComponent.sendAcivationEmail(new UserDto(user), generatedString);
        }
        return user;
    }

    public UserEntity createUser(
            String username,
            String password,
            String firstname,
            String lastname,
            String email,
            String phone,
            List<String> roles) throws ServiceException {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(password);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(false);

        Map<String, String> principles = new HashMap<>();
        principles.put("username", username);
        principles.put("firstname", firstname);
        principles.put("lastname", lastname);
        principles.put("email", email);
        principles.put("phone", phone);

        user.setPrinciples(principles);

        if (userEntityRepository.findByUsername(username).isPresent()) {
            throw new ServiceException(Error.user_exist);
        };

        userEntityRepository.save(user);
        roles.forEach(roleName -> {
            Optional<RoleEntity> role = roleEntityRepository.findByName(roleName);
            if (role.isPresent()) {
                RoleEntity r = role.get();
                r.getUsers().add(user);
                user.getRoles().add(r);
            }
        });
        userEntityRepository.save(user);
        return user;
    }

    public boolean deactivateUser(String username) throws ServiceException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));
        if (user.isSystem()) {
            throw new ServiceException(Error.cannot_deactivate_sys_user);
        }
        user.setEnabled(false);
        userEntityRepository.save(user);
        return true;
    }

    public String changeUserPassword(String username, String newPassword) throws ServiceException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));
        user.setPassword(newPassword);
        userEntityRepository.save(user);
        return user.getPassword();
    }

    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }
    
    public Page<UserEntity> getAllUsers(int page, int size) {
        return userEntityRepository.findAll(PageRequest.of(page, size));
    }

    public List<UserEntity> getAllUsersByRole(String role) {
        Optional<RoleEntity> roleEntity = roleEntityRepository.findByName(role);
        if (roleEntity.isPresent()) {
            return new ArrayList<>(roleEntity.get().getUsers());
        }
        return new ArrayList<>();
    }

    public List<UserEntity> getAllUsersByPrivilege(String privilege) {
        return grantedPrivilegeEntityRepository.findByValue(privilege)
                .stream()
                .map(p -> p.getRole())
                .map(r -> r.getUsers())
                .flatMap(r -> r.stream())
                .collect(Collectors.toList());
    }

    public UserEntity updateUserProfile(String username, Map<String, String> principles) throws ServiceException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));

        principles.keySet().forEach(key -> {
            user.getPrinciples().put(key.toLowerCase(), principles.get(key));
        });

        return userEntityRepository.save(user);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws ServiceException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new ServiceException(Error.user_not_exist));
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
        }
        return userEntityRepository.save(user).getPassword().equals(newPassword);
    }

}
