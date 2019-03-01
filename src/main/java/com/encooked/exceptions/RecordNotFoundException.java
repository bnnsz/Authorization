/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.exceptions;

/**
 *
 * @author obinna.asuzu
 */
public class RecordNotFoundException extends Exception{
    public RecordNotFoundException(String message) {
        super(message);
    }

    public RecordNotFoundException(Throwable ex) {
        super(ex);
    }

    public RecordNotFoundException(String message, Throwable ex) {
        super(message, ex);
    }
}
