/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.services;

import com.encooked.entities.PriviledgeEntity;
import com.encooked.entities.RoleEntity;
import com.encooked.entities.UserEntity;
import com.encooked.exceptions.RecordExistsException;
import com.encooked.exceptions.RecordNotFoundException;
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
import org.springframework.security.core.GrantedAuthority;
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
            String phone) {
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
        return user;
    }

    public boolean deactivateUser(String username) {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
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
        Optional<PriviledgeEntity> priviledgeEntity = priviledgeEntityRepository.findByValue(priviledge);
        if (priviledgeEntity.isPresent()) {
            return priviledgeEntity.get()
                    .getRoles().stream()
                    .map(r -> r.getUsers())
                    .flatMap(r -> r.stream())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public RoleEntity createRole(String name) throws RecordNotFoundException {
        roleEntityRepository.findByName(name)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Role \"%s\" already exist", name)));
        
        RoleEntity role = new RoleEntity();
        role.setName(name);
        return roleEntityRepository.save(role);
    }

    public GrantedAuthority createAuthority(GrantedAuthority authority) throws RecordExistsException, RecordNotFoundException {
        String role = authority.getAuthority().split(".")[0];
        String priviledge = authority.getAuthority().split(".")[0];

        RoleEntity roleEntity = roleEntityRepository.findByName(role)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Role \"%s\" does not exist", role)));

        PriviledgeEntity priviledgeEntity = priviledgeEntityRepository.findByValue(priviledge)
                .orElseThrow(() -> new RecordNotFoundException((String.format("Priviledge \"%s\" does not exist", priviledge))));

        roleEntity.getPriviledges().stream().filter(p -> p.getValue().equals(priviledge)).findFirst()
                .ifPresent(e -> new RecordExistsException("Authority already exists"));

        
        priviledgeEntity.getRoles().add(roleEntity);
        roleEntity.getPriviledges().add(priviledgeEntity);
        
        return priviledgeEntity.of(roleEntityRepository.save(roleEntity));
    }

    public void removeAuthority(GrantedAuthority authority) throws RecordNotFoundException {
        String role = authority.getAuthority().split(".")[0];
        String priviledge = authority.getAuthority().split(".")[0];

        RoleEntity roleEntity = roleEntityRepository.findByName(role)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Role \"%s\" does not exist", role)));

        PriviledgeEntity priviledgeEntity = priviledgeEntityRepository.findByValue(priviledge)
                .orElseThrow(() -> new RecordNotFoundException((String.format("Priviledge \"%s\" does not exist", priviledge))));
        
        roleEntity.getPriviledges().remove(priviledgeEntity);
        
        roleEntityRepository.save(roleEntity);
    }

    public int deleteRole(String name) {
        return roleEntityRepository.deleteByName(name).size();
    }

    public PriviledgeEntity createPriviledges(String value) throws RecordNotFoundException{
        priviledgeEntityRepository.findByValue(value)
                .ifPresent(p -> new RecordExistsException((String.format("Priviledge \"%s\" already exist", p))));
        
        PriviledgeEntity priviledgeEntity = new PriviledgeEntity();
        priviledgeEntity.setValue(value);
        return priviledgeEntityRepository.save(priviledgeEntity);
    }

    public UserEntity updateUserProfile(String username, Map<String, String> principles) throws UsernameNotFoundException{
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
        
        principles.keySet().forEach(key -> {
            user.getPrinciples().put(key.toLowerCase(), principles.get(key));
        });
        
       return  userEntityRepository.save(user);
    }

}
