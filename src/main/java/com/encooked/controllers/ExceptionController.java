/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.controllers;

import com.encooked.dto.ErrorResponse;
import com.encooked.exceptions.RecordExistsException;
import com.encooked.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 *
 * @author obinna.asuzu
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler({DisabledException.class, LockedException.class, CredentialsExpiredException.class, AccountExpiredException.class})
    public ResponseEntity<?> handleDisabledException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ex));
    }

    @ExceptionHandler({UsernameNotFoundException.class, AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<?> handleUsernameNotFoundException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex));
    }
    
    @ExceptionHandler({RecordExistsException.class})
    public ResponseEntity<?> handleRecordExistException(Throwable ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(ex));
    }
    
    @ExceptionHandler({ServiceException.class})
    public ResponseEntity<?> handleServiceException(ServiceException ex) {
        return ResponseEntity.status(ex.getError().getStatus()).body(new ErrorResponse(ex));
    }
}
