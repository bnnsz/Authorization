/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.entities;

import com.encooked.entities.enums.AuditLogAction;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author ObinnaAsuzu
 */
@Entity
public class AuditLogEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="LOG_TIMESTAMP")
    private LocalDateTime timestamp;
    
    @Column
    private String username;
    
    @Column(name="LOG_ACTION")
    @Enumerated(EnumType.ORDINAL)
    private AuditLogAction action;
    
    @Column
    private String entityName;
    
    @Column
    private String entityRef;
    
    @Column
    private String entityState;
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the timestamp
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the action
     */
    public AuditLogAction getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(AuditLogAction action) {
        this.action = action;
    }

    

    /**
     * @return the entityName
     */
    public String getEntityName() {
        return entityName;
    }

    /**
     * @param entityName the entityName to set
     */
    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    /**
     * @return the entityRef
     */
    public String getEntityRef() {
        return entityRef;
    }

    /**
     * @param entityRef the entityRef to set
     */
    public void setEntityRef(String entityRef) {
        this.entityRef = entityRef;
    }

    /**
     * @return the entityState
     */
    public String getEntityState() {
        return entityState;
    }

    /**
     * @param entityState the entityState to set
     */
    public void setEntityState(String entityState) {
        this.entityState = entityState;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuditLogEntity)) {
            return false;
        }
        AuditLogEntity other = (AuditLogEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.encooked.security.entities.AuditLogEntity[ id=" + id + " ]";
    }
    
}
