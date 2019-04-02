/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.dto;

import java.io.Serializable;
import org.springframework.hateoas.ResourceSupport;

/**
 *
 * @author obinna.asuzu
 * @param <T>
 */
public class Resource<T> extends ResourceSupport implements Serializable {
    private T data;
    public Resource() {
    }

    public Resource(T data) {
        this.data = data;
    }

    /**
     * @return the data
     */
    public T getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(T data) {
        this.data = data;
    }
    
}
