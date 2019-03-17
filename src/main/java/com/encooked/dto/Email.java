/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.dto;

import com.encooked.enums.EmailType;
import io.swagger.annotations.ApiModel;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author obinna.asuzu
 */
@ApiModel(value = "Email", description = "Email payload: headers, body and attachments")
public class Email {

    private List<Address> to = new ArrayList<>();
    private List<Address> cc = new ArrayList<>();
    private List<Address> bcc = new ArrayList<>();
    private String subject;
    private EmailType type = EmailType.TEXT;
    private String body;
    private List<Attachment> attachments = new ArrayList<>();

    public Email() {
    }

    /**
     * @return the to
     */
    public List<Address> getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(List<Address> to) {
        this.to = to;
    }

    /**
     * @return the cc
     */
    public List<Address> getCc() {
        return cc;
    }

    /**
     * @param cc the cc to set
     */
    public void setCc(List<Address> cc) {
        this.cc = cc;
    }

    /**
     * @return the bcc
     */
    public List<Address> getBcc() {
        return bcc;
    }

    /**
     * @param bcc the bcc to set
     */
    public void setBcc(List<Address> bcc) {
        this.bcc = bcc;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the type
     */
    public EmailType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(EmailType type) {
        this.type = type;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }
    
    /**
     * @param body
     * @param type
     */
    public void setContent(String body,EmailType type) {
        this.type = type;
        this.body = body;
    }

    /**
     * @return the attachments
     */
    public List<Attachment> getAttachments() {
        return attachments;
    }

    /**
     * @param attachments the attachments to set
     */
    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

}
