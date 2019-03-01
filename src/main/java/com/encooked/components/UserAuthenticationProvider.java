/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import com.encooked.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 *
 * @author obinna.asuzu
 */
@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        
        if(!authentication.getAuthorities().isEmpty() && authentication.isAuthenticated()){
            return authentication;
        }

        UserDetails user = userService.getUser(username);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("Invalid username or password");
        }

        if (!user.isAccountNonExpired()) {
            throw new AccountExpiredException("Account has expired");
        }

        if (!user.isAccountNonLocked()) {
            throw new LockedException("Account has been locked");
        }

        if (!user.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("Credentials expired");
        }

        if (!user.isEnabled()) {
            throw new DisabledException("Account has been disabled");
        }

        if (!user.getUsername().equals(username) || !password.equals(user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    
    

}
