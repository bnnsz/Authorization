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
public class RecordExistsException extends Exception {

    public RecordExistsException(String message) {
        super(message);
    }

    public RecordExistsException(Throwable ex) {
        super(ex);
    }

    public RecordExistsException(String message, Throwable ex) {
        super(message, ex);
    }

}
