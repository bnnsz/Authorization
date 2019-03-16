/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author obinna.asuzu
 */
@Entity
public class GrantedPriviledgeEntity extends PriviledgeEntity implements Serializable {

    @Column
    private boolean read;
    @Column
    private boolean write;

    @ManyToMany(mappedBy = "priviledges")
    private Set<RoleEntity> roles = new HashSet<>();

    public GrantedPriviledgeEntity() {
    }

    public GrantedPriviledgeEntity(String value) {
        setValue(value);
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return the write
     */
    public boolean isWrite() {
        return write;
    }

    /**
     * @param write the write to set
     */
    public void setWrite(boolean write) {
        this.write = write;
    }

    /**
     * @return the roles
     */
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    /**
     * @param roles the roles to set
     */
    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (super.hashCode());
        hash = 53 * hash + (this.read ? 1 : 0);
        hash = 53 * hash + (this.write ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GrantedPriviledgeEntity other = (GrantedPriviledgeEntity) obj;
        
        
        if ((this.getId() == null || other.getId() == null)) {
            return (this.getValue().equals(other.getValue()) && this.read == other.read && this.write == other.write);
        }
        return (this.getId() != null && !this.getId().equals(other.getId()));
    }

    @Override
    public String toString() {
        return "com.encooked.data.entities.GrantedPriviledgeEntity[ id=" + getId() + " ]";
    }

}
