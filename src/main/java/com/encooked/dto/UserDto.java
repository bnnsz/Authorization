/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.dto;

import com.encooked.entities.UserEntity;
import com.encooked.entities.UserPrincipalEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import static java.util.stream.Collectors.toMap;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author obinna.asuzu
 */
@ApiModel(value = "User", description = "User infomation, credentials and principles")
@JsonInclude(Include.NON_NULL)
public class UserDto extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 1009L;
    @ApiModelProperty(value = "username of User")
    private String username;
    @ApiModelProperty(value = "password of User", readOnly = true)
    private String password;
    @ApiModelProperty(value = "User principles")
    private Map<String, String> principles = new HashMap<>();
    @ApiModelProperty(value = "user's granted authorities")
    private List<String> grantedAuthorities = new ArrayList<>();
    @ApiModelProperty(value = "true if user account is enabled otherwise false")
    private Boolean enabled;
    @ApiModelProperty(value = "true if user account is not locked otherwise false")
    private Boolean accountNonLocked;
    @ApiModelProperty(value = "true if user's credential is not expired otherwise false")
    private Boolean credentialsNonExpired;
    @ApiModelProperty(value = "true if user account is not expired otherwise false")
    private Boolean accountNonExpired;
    @ApiModelProperty(value = "Indicates that user account is a system accoun. System account cannot be deactivated", readOnly = true)
    private Boolean system;
    @ApiModelProperty(value = "User roles")
    private List<String> roles = new ArrayList<>();

    public UserDto() {

    }

    public UserDto(UserDetails other) {
        this.username = other.getUsername();
//        this.password = other.getPassword();
        this.grantedAuthorities = other.getAuthorities().stream().map(g -> g.getAuthority()).collect(Collectors.toList());
        this.accountNonExpired = other.isAccountNonExpired();
        this.accountNonLocked = other.isAccountNonLocked();
        this.credentialsNonExpired = other.isCredentialsNonExpired();
        this.enabled = other.isEnabled();
        this.roles = this.grantedAuthorities.stream().filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.split("_")[1])
                .collect(Collectors.toList());
    }

    public UserDto(UserEntity other) {
        this.username = other.getUsername();
//        this.password = other.getPassword();
        this.grantedAuthorities = other.getAuthorities().stream().map(g -> g.getAuthority()).collect(Collectors.toList());
        this.accountNonExpired = other.isAccountNonExpired();
        this.accountNonLocked = other.isAccountNonLocked();
        this.credentialsNonExpired = other.isCredentialsNonExpired();
        this.enabled = other.isEnabled();
        this.principles = other.getPrincipals().stream().collect(toMap(UserPrincipalEntity::getKey, UserPrincipalEntity::getValue));
        this.system = other.isSystem();
        this.roles = this.grantedAuthorities.stream().filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.split("_")[1])
                .collect(Collectors.toList());
    }

    public UserDto(String username) {
        this.username = username;
        this.principles = null;
        this.roles = null;
        this.grantedAuthorities = null;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public Boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public Boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the principles
     */
    public Map<String, String> getPrinciples() {
        return principles;
    }

    /**
     * @param principles the principles to set
     */
    public void setPrinciples(Map<String, String> principles) {
        this.principles = principles;
    }

    /**
     * @return the grantedAuthorities
     */
    public List<String> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    /**
     * @param grantedAuthorities the grantedAuthorities to set
     */
    public void setGrantedAuthorities(List<String> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param accountNonLocked the accountNonLocked to set
     */
    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * @param credentialsNonExpired the credentialsNonExpired to set
     */
    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * @param accountNonExpired the accountNonExpired to set
     */
    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    /**
     * @return the system
     */
    public Boolean isSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(Boolean system) {
        this.system = system;
    }

    /**
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Boolean validate(UserDto userDetails) {
        String otherUser = username + ":" + grantedAuthorities + ":"
                + accountNonExpired + ":" + accountNonLocked + ":"
                + credentialsNonExpired + ":" + enabled;

        String thisUser = userDetails.username + ":" + userDetails.grantedAuthorities + ":"
                + userDetails.accountNonExpired + ":" + userDetails.accountNonLocked + ":"
                + userDetails.credentialsNonExpired + ":" + userDetails.enabled;

        return thisUser.equals(otherUser);
    }

}
