/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.services;

import com.encooked.entities.GrantedPriviledgeEntity;
import com.encooked.entities.PriviledgeEntity;
import com.encooked.entities.RoleEntity;
import com.encooked.exceptions.RecordExistsException;
import com.encooked.exceptions.RecordNotFoundException;
import com.encooked.repositories.GrantedPriviledgeEntityRepository;
import com.encooked.repositories.PriviledgeEntityRepository;
import com.encooked.repositories.RoleEntityRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 *
 * @author obinna.asuzu
 */
@Service
public class AuthorityService {

    @Autowired
    UserService userService;

    @Autowired
    RoleEntityRepository roleEntityRepository;

    @Autowired
    PriviledgeEntityRepository priviledgeEntityRepository;

    @Autowired
    GrantedPriviledgeEntityRepository grantedPriviledgeEntityRepository;

    public List<String> getAllRoles(boolean indicateSystem) {
        return roleEntityRepository.findAll()
                .stream().map(p -> p.getName() + (indicateSystem ? p.isSystem() ? "*" : "" : ""))
                .collect(Collectors.toList());
    }

    public List<String> getAllRoles() {
        return getAllRoles(false);
    }

    public List<String> getAllRolesByPriviledge(String value, boolean indicateSystem) throws RecordNotFoundException {
        return grantedPriviledgeEntityRepository.findByValue(value)
                .stream()
                .map(p -> p.getRole().getName() + (indicateSystem ? p.getRole().isSystem() ? "*" : "" : ""))
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> getAllRolesByPriviledge(String value) throws RecordNotFoundException {
        return getAllRolesByPriviledge(value, false);
    }

    public List<String> getAllAuthorities() {
        List<String> grantedAuthorities = new ArrayList<>();
        roleEntityRepository.findAll().forEach(role -> {
            role.getPriviledges().forEach(priv -> {
                String a = role.getName() + "." + priv.getValue();
                if (priv.isRead()) {
                    grantedAuthorities.add(a.toUpperCase() + "_READ");
                }

                if (priv.isWrite()) {
                    grantedAuthorities.add(a.toUpperCase() + "_WRITE");
                }
            });
        });
        return grantedAuthorities;
    }

    public List<String> getAllPermission(boolean indicateSystem) {
        return priviledgeEntityRepository.findAll()
                .stream().map(p -> p.getValue() + (indicateSystem ? p.isSystem() ? "*" : "" : ""))
                .collect(Collectors.toList());
    }

    public List<String> getAllPermission() {
        return getAllPermission(false);
    }

    public List<String> getAllPermissionsByRole(String role, boolean indicateSystem) throws RecordNotFoundException {
        return roleEntityRepository.findByName(role)
                .orElseThrow(() -> new RecordNotFoundException("Role " + role + " does not exist"))
                .getPriviledges().stream().map(p -> p.getValue() + (indicateSystem ? p.isSystem() ? "*" : "" : ""))
                .collect(Collectors.toList());
    }

    public List<String> getAllPermissionsByRole(String role) throws RecordNotFoundException {
        return getAllPermissionsByRole(role, false);
    }

    public RoleEntity createRole(String name) throws Exception {
        if (roleEntityRepository.findByName(name).isPresent()) {
            throw new Exception(String.format("Role '%s\' already exist", name));
        }
        RoleEntity role = new RoleEntity();
        role.setName(name);
        return roleEntityRepository.save(role);
    }

    private void validateAuthority(String value) throws Exception {
        value = value.trim();
        if (value.split("\\.").length != 2) {
            throw new Exception("Invalid Authority.Valid format: ROLE.PERMISSION_ACCESS Example: \"ADMIN.USER_READ\"");
        }

        if (value.split("_").length != 2) {
            throw new Exception("Invalid Authority.Valid format: ROLE.PERMISSION_ACCESS Example: \"ADMIN.USER_READ\"");
        }

        Pattern p = Pattern.compile("^[a-zA-Z0-9._-]*$", Pattern.CASE_INSENSITIVE);
        if (!p.matcher(value).matches()) {
            throw new Exception("Invalid Authority: Allowed characters; a to z, A to Z, 0 to 9,\"_\" \"_\" and \"-\"  Examples: \"ADMIN.USER_READ\", \"ADMIN.WEB-SOCKET_READ\"");
        }

        if (!value.endsWith("_READ") && !value.endsWith("_WRITE")) {
            throw new Exception("Invalid Authority: Must end with \"_READ\" or \"_WRITE\"  Examples: \"ADMIN.USER_READ\", \"ADMIN.WEB-SOCKET_WRITE\"");
        }
    }

    private GrantedAuthority authorize(GrantedAuthority authority, boolean grant) throws RecordNotFoundException, Exception {
        String value = authority.getAuthority().toUpperCase();
        validateAuthority(value);

        String[] split = authority.getAuthority().split("\\.");

        String role = split[0];
        String priviledge_access = split[1];

        split = priviledge_access.split("_");

        String priviledge = split[0];
        String access = split[1];

        RoleEntity roleEntity = roleEntityRepository.findByName(role)
                .orElseThrow(() -> new RecordNotFoundException(String.format("Role \"%s\" does not exist", role)));

        priviledgeEntityRepository.findByValue(priviledge)
                .orElseThrow(() -> new RecordNotFoundException((String.format("Priviledge \"%s\" does not exist", priviledge))));

        GrantedPriviledgeEntity grantedPriviledgeEntity = grantedPriviledgeEntityRepository.findByValue(priviledge)
                .stream()
                .filter(p -> p.getRole().getName().equals(role))
                .findFirst().orElseGet(() -> {
                    return grantedPriviledgeEntityRepository.save(new GrantedPriviledgeEntity(priviledge));
                });

        grantedPriviledgeEntity.setRead(access.equals("READ") ? grant : grantedPriviledgeEntity.isRead());
        grantedPriviledgeEntity.setWrite(access.equals("WRITE") ? grant : grantedPriviledgeEntity.isWrite());

        grantedPriviledgeEntity.setRole(roleEntity);
        roleEntity.getPriviledges().add(grantedPriviledgeEntity);
        roleEntityRepository.save(roleEntity);

        return authority;
    }

    public GrantedAuthority createAuthority(GrantedAuthority authority) throws RecordExistsException, RecordNotFoundException, Exception {
        return authorize(authority, true);
    }

    public void removeAuthority(GrantedAuthority authority) throws RecordNotFoundException, Exception {
        authorize(authority, false);
    }

    public int deleteRole(String name) throws Exception {
        if (!userService.getAllUsersByRole(name).isEmpty()) {
            throw new Exception("You cannot delete roles that are already in use");
        }
        RoleEntity role = roleEntityRepository.findByName(name).orElseThrow(() -> new Exception("Role does not exist"));
        if (role.isSystem()) {
            throw new Exception("You cannot delete a system role");
        }
        return roleEntityRepository.deleteByName(name).size();
    }

    public int deletePriviledge(String value) throws Exception {
        if (!getAllRolesByPriviledge(value).isEmpty()) {
            throw new Exception("You cannot delete priviledges that are already in use");
        }
        PriviledgeEntity priviledge = priviledgeEntityRepository.findByValue(value).orElseThrow(() -> new Exception("Priviledge does not exist"));
        if (priviledge.isSystem()) {
            throw new Exception("You cannot delete a system priviledge");
        }
        return priviledgeEntityRepository.deleteByValue(value).size();
    }

    public PriviledgeEntity createPriviledge(String value) throws Exception {
        if (priviledgeEntityRepository.findByValue(value).isPresent()) {
            throw new Exception(String.format("Priviledge '%s\' already exist", value));
        }

        PriviledgeEntity priviledgeEntity = new PriviledgeEntity();
        priviledgeEntity.setValue(value);
        return priviledgeEntityRepository.save(priviledgeEntity);
    }

}
