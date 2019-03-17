/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.entities;

import com.encooked.components.EntityListener;
import com.google.gson.Gson;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author ObinnaAsuzu
 */
@EntityListeners(EntityListener.class)
@MappedSuperclass
public abstract class AbstractEntity {

    @Column
    private LocalDateTime createdTimestamp;

    @Column
    private LocalDateTime updatedTimestamp;

    public abstract Long getId();

    /**
     * @return the createdTimestamp
     */
    public LocalDateTime getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * @param createdTimestamp the createdTimestamp to set
     */
    public void setCreatedTimestamp(LocalDateTime createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    /**
     * @return the updatedTimestamp
     */
    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    /**
     * @param updatedTimestamp the updatedTimestamp to set
     */
    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }
    
    public String getState(){
        return new Gson().toJson(this);
    }

}
