/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import com.encooked.config.FreemarkerConfig;
import com.encooked.dto.Address;
import com.encooked.dto.Email;
import com.encooked.dto.UserDto;
import com.encooked.enums.EmailType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

/**
 *
 * @author obinna.asuzu
 */
@Component
public class MessageComponent {

    @Autowired
    FreemarkerConfig freemarkerConfig;

    public boolean sendAcivationEmail(UserDto user, String activationToken) throws IOException, TemplateException {
        Template t = freemarkerConfig.getTemplate("activate_template.html");

        Map model = new HashMap();
        model.put("title", "Welcome " + user.getPrinciples().get("firstname"));
        model.put("heading", "To Encooked");
        model.put("message", "You are on step away from completing your registeration, click the button below to continue.");
        model.put("activation.url", "http://app.encooked.com/activate?token=" + activationToken);

        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost/messaging/send";
        Email email = new Email();
        email.getTo().add(new Address(user.getPrinciples().get("email")));
        email.setSubject("Welcome to Encooked");
        email.setContent(html, EmailType.HTML);

        return restTemplate.postForObject(url, email, Boolean.class);
    }
}
