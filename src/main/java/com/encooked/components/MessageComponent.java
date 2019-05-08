/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import com.encooked.dto.Address;
import com.encooked.dto.UserDto;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author obinna.asuzu
 */
@Component
public class MessageComponent {

    @Autowired
    private EurekaClient discoveryClient;

    @Async
    public void sendAcivationEmail(UserDto user, String activationToken) {

        InstanceInfo instance = discoveryClient.getNextServerFromEureka("MESSAGING", false);
        String messagingUrl = instance.getHomePageUrl();

        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        String firstname = user.getPrinciples().get("firstname");
        String email = user.getPrinciples().get("email");
        HttpEntity<Address> request = new HttpEntity<>(new Address(firstname, email));
        String url = messagingUrl + "api/v1/email/send/activate/" + activationToken;
        restTemplate.postForObject(url, request, Boolean.class);
    }
}
