/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author obinna.asuzu
 */
@ApiModel(value = "ErrorResponse",description = "Returned whenever an error occurs")
public class ErrorResponse {
    @ApiModelProperty(value = "Date/time of error",example = "31 January 2001 10:30:54 am")
    private String date;
    @ApiModelProperty(value = "Message indicating the cause of the error")
    private String message;

    public ErrorResponse(String message) {
        this.date = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a").format(new Date());
        this.message = message;
    }

    public ErrorResponse(Throwable ex) {
        this.date = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss a").format(new Date());
        this.message = ex.getMessage();

    }

    /**
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
