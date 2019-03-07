/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.dto;

import com.encooked.entities.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author obinna.asuzu
 */
@ApiModel(value = "User", description = "User infomation, credentials and principles")
public class UserDto implements UserDetails, Serializable {

    @ApiModelProperty(value = "username of User")
    private String username;
    @ApiModelProperty(value = "password of User")
    private String password;
    @ApiModelProperty(value = "User principles")
    private Map<String, String> principles = new HashMap<>();
    @ApiModelProperty(value = "user's granted authorities")
    private String grantedAuthorities;
    @ApiModelProperty(value = "true if user account is enabled otherwise false")
    private boolean enabled;
    @ApiModelProperty(value = "true if user account is not locked otherwise false")
    private boolean accountNonLocked;
    @ApiModelProperty(value = "true if user's credential is not expired otherwise false")
    private boolean credentialsNonExpired;
    @ApiModelProperty(value = "true if user account is not expired otherwise false")
    private boolean accountNonExpired;

    public UserDto(UserDetails other) {
        this.username = other.getUsername();
        this.password = other.getPassword();
        this.grantedAuthorities = other.getAuthorities().stream().map(g -> g.toString()).collect(Collectors.joining(","));
        this.accountNonExpired = other.isAccountNonExpired();
        this.accountNonLocked = other.isAccountNonLocked();
        this.credentialsNonExpired = other.isCredentialsNonExpired();
        this.enabled = other.isEnabled();
    }

    public UserDto(UserEntity other) {
        this.username = other.getUsername();
        this.password = other.getPassword();
        this.grantedAuthorities = other.getAuthorities().stream().map(g -> g.toString()).collect(Collectors.joining(","));
        this.accountNonExpired = other.isAccountNonExpired();
        this.accountNonLocked = other.isAccountNonLocked();
        this.credentialsNonExpired = other.isCredentialsNonExpired();
        this.enabled = other.isEnabled();
        this.principles = other.getPrinciples();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(grantedAuthorities.split(",")).stream()
                .map(g -> (GrantedAuthority) () -> g).collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
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
    public String getGrantedAuthorities() {
        return grantedAuthorities;
    }

    /**
     * @param grantedAuthorities the grantedAuthorities to set
     */
    public void setGrantedAuthorities(String grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @param accountNonLocked the accountNonLocked to set
     */
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    /**
     * @param credentialsNonExpired the credentialsNonExpired to set
     */
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    /**
     * @param accountNonExpired the accountNonExpired to set
     */
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean validate(UserDto userDetails) {
        String otherUser = username + ":" + grantedAuthorities + ":"
                + accountNonExpired + ":" + accountNonLocked + ":"
                + credentialsNonExpired + ":" + enabled;

        String thisUser = userDetails.username + ":" + userDetails.grantedAuthorities + ":"
                + userDetails.accountNonExpired + ":" + userDetails.accountNonLocked + ":"
                + userDetails.credentialsNonExpired + ":" + userDetails.enabled;

        return thisUser.equals(otherUser);
    }

}
