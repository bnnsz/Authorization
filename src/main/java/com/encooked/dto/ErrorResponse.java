/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.dto;

import java.util.Date;

/**
 *
 * @author obinna.asuzu
 */
public class ErrorResponse {

    Date date;
    String message;

    public ErrorResponse(String message) {
        this.date = new Date();
        this.message = message;
    }

    public ErrorResponse(Throwable ex) {
        this.date = new Date();
        this.message = ex.getMessage();

    }

}
