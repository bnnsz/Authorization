/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.config;

import com.encooked.components.RequestListener;
import com.encooked.components.UserAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 *
 * @author obinna.asuzu
 */
@Configuration
@EnableJpaAuditing
public class ServerConfig extends WebSecurityConfigurerAdapter {

    

    @Autowired
    UserAuthenticationProvider authenticationProvider;
    
    @Autowired
    RequestListener requestListener;

    @Autowired
    protected void globalConfig(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(requestListener, BasicAuthenticationFilter.class).authorizeRequests()
                .antMatchers("/signup","/*").permitAll()
                .anyRequest().permitAll()
                .and()
                .httpBasic()
                .and().csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

}
