/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.services;

import com.encooked.entities.GrantedPriviledgeEntity;
import com.encooked.entities.RoleEntity;
import com.encooked.entities.UserEntity;
import com.encooked.repositories.GrantedPriviledgeEntityRepository;
import com.encooked.repositories.PriviledgeEntityRepository;
import com.encooked.repositories.RoleEntityRepository;
import com.encooked.repositories.UserEntityRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author obinna.asuzu
 */
@Service
public class UserService {

    @Autowired
    UserEntityRepository userEntityRepository;

    @Autowired
    RoleEntityRepository roleEntityRepository;

    @Autowired
    PriviledgeEntityRepository priviledgeEntityRepository;

    @Autowired
    GrantedPriviledgeEntityRepository grantedPriviledgeEntityRepository;
    
    
    
    public UserDetails activateUser(String username) throws Exception, UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        if(user.isEnabled()){
            throw new Exception("Account is already active");
        }
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        userEntityRepository.save(user);
        return user;
    }
    
    public UserDetails getUser(String username) {
        return userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

    public Map<String, String> getUserProfile(String username) {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        return user.getPrinciples();
    }

    public UserEntity createUser(
            String username,
            String password,
            String firstname,
            String lastname,
            String email,
            String phone,
            List<String> roles) {
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

        userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User already exist"));
        
        
        userEntityRepository.save(user);
        roles.forEach(roleName -> {
            Optional<RoleEntity> role = roleEntityRepository.findByName(roleName);
            if(role.isPresent()){
                RoleEntity r = role.get();
                r.getUsers().add(user);
                user.getRoles().add(r);
            }
        });
        userEntityRepository.save(user);
        return user;
    }

    public boolean deactivateUser(String username) throws Exception {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        if(user.isSystem()){
            throw new Exception("You cannot deactivate a system user");
        }
        user.setEnabled(false);
        userEntityRepository.save(user);
        return true;
    }

    public String changeUserPassword(String username, String newPassword) {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        user.setPassword(newPassword);
        userEntityRepository.save(user);
        return user.getPassword();
    }

    public List<UserEntity> getAllUsers() {
        return userEntityRepository.findAll();
    }

    public List<UserEntity> getAllUsersByRole(String role) {
        Optional<RoleEntity> roleEntity = roleEntityRepository.findByName(role);
        if (roleEntity.isPresent()) {
            return new ArrayList<>(roleEntity.get().getUsers());
        }
        return new ArrayList<>();
    }

    public List<UserEntity> getAllUsersByPriviledge(String priviledge) {
        return grantedPriviledgeEntityRepository.findByValue(priviledge)
                .stream()
                .map(p -> p.getRole())
                .map(r -> r.getUsers())
                    .flatMap(r -> r.stream())
                    .collect(Collectors.toList());
    }

     public UserEntity updateUserProfile(String username, Map<String, String> principles) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        principles.keySet().forEach(key -> {
            user.getPrinciples().put(key.toLowerCase(), principles.get(key));
        });

        return userEntityRepository.save(user);
    }

    public boolean changePassword(String username, String oldPassword, String newPassword) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
        }
        return userEntityRepository.save(user).getPassword().equals(newPassword);
    }

}
