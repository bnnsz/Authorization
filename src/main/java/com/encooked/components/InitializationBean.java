/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import com.encooked.entities.GrantedPriviledgeEntity;
import com.encooked.entities.PriviledgeEntity;
import com.encooked.entities.RoleEntity;
import com.encooked.entities.UserEntity;
import com.encooked.repositories.GrantedPriviledgeEntityRepository;
import com.encooked.repositories.PriviledgeEntityRepository;
import com.encooked.repositories.RoleEntityRepository;
import com.encooked.repositories.UserEntityRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author obinna.asuzu
 */
@Component
public class InitializationBean {

    @Autowired
    UserEntityRepository userEntityRepository;
    @Autowired
    PriviledgeEntityRepository priviledgeEntityRepository;
    @Autowired
    GrantedPriviledgeEntityRepository grantedPriviledgeEntityRepository;
    @Autowired
    RoleEntityRepository roleEntityRepository;
    
    @PostConstruct
    public void init(){
        try {
            initRoles();
            initPriviledges();
            initGrants();
            initUsers();
        } catch (Exception ex) {
            Logger.getLogger(InitializationBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initRoles() {
        if (roleEntityRepository.count() == 0) {
            Arrays.asList("ADMIN", "VENDOR", "CLIENT", "SUPPLIER")
                    .forEach((role) -> roleEntityRepository.save(new RoleEntity(role,true)));
        }
    }

    public void initPriviledges() {
        if (priviledgeEntityRepository.count() == 0) {
            Arrays.asList("USER", "EMAIL", "SMS", "SWAGGER", "ACTUATOR", "LOG")
                    .forEach((priv) -> priviledgeEntityRepository.save(new PriviledgeEntity(priv,true)));
        }
    }

    public void initGrants() throws Exception {
        for (String role : Arrays.asList("ADMIN", "VENDOR", "CLIENT", "SUPPLIER")) {
            Optional<RoleEntity> option = roleEntityRepository.findByName(role);
            RoleEntity roleEntity = option.orElseThrow(() -> new Exception());
            List<String> priviledges = new ArrayList<>();
            switch (role) {
                case "ADMIN":
                    priviledges.addAll(Arrays.asList("SWAGGER", "ACTUATOR", "LOG"));
                    break;
            }
            priviledges.addAll(Arrays.asList("USER", "EMAIL", "SMS"));

            for (String priviledge : priviledges) {
                GrantedPriviledgeEntity gpe = new GrantedPriviledgeEntity(priviledge, true, true);
                gpe = grantedPriviledgeEntityRepository.save(gpe);
                gpe.setRole(roleEntity);
                roleEntity.getPriviledges().add(gpe);
                roleEntityRepository.save(roleEntity);
            }

        };
    }

    public void initUsers() throws Exception {
        if (userEntityRepository.count() == 0) {
            RoleEntity role = roleEntityRepository.findByName("ADMIN").orElseThrow(() -> new Exception());
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword("admin");
            user.getPrinciples().put("firstname", "Admin");
            user.setCredentialsNonExpired(false);
            user.setEnabled(true);
            user.setAccountNonLocked(true);
            user.setAccountNonExpired(true);
            user.setSystem(true);
            user = userEntityRepository.save(user);
            user.getRoles().add(role);
            role.getUsers().add(user);
            userEntityRepository.save(user);
        }
    }
}
