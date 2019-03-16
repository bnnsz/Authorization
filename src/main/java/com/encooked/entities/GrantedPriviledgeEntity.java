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

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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
        GrantedPriviledgeEntity other = (GrantedPriviledgeEntity) object;
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
