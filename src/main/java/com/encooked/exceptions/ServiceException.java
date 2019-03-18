/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.exceptions;

import com.encooked.enums.Error;

/**
 *
 * @author obinna.asuzu
 */
public class ServiceException extends Exception{
    Error error;

    public ServiceException(Error code) {
        this.error = code;
    }

    @Override
    public String getMessage() {
        return error.getMessage();
    }
    
    public Error getError() {
        return error;
    }
}
