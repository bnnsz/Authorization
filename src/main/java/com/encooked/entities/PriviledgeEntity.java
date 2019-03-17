/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author obinna.asuzu
 */
@Entity
public class PriviledgeEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String value;

    @Column
    private boolean system;

    public PriviledgeEntity() {
    }

    public PriviledgeEntity(String value) {
        this.value = value;
    }

    public PriviledgeEntity(String value, boolean system) {
        this.value = value;
        this.system = system;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the system
     */
    public boolean isSystem() {
        return system;
    }

    /**
     * @param system the system to set
     */
    public void setSystem(boolean system) {
        this.system = system;
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
        if (!(object instanceof GrantedPriviledgeEntity)) {
            return false;
        }
        PriviledgeEntity other = (PriviledgeEntity) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.encooked.data.entities.PriviledgeEntity[ id=" + id + " ]";
    }
}
