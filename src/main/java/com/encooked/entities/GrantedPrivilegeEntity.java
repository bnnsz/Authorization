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
import javax.persistence.ManyToOne;

/**
 *
 * @author obinna.asuzu
 */
@Entity
public class GrantedPrivilegeEntity extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String value;
    @Column(name="gp_read")
    private boolean read;
    @Column(name="gp_write")
    private boolean write;
    @Column
    private boolean system;

    @ManyToOne
    private RoleEntity role;

    public GrantedPrivilegeEntity() {
    }

    public GrantedPrivilegeEntity(String value) {
        this.value = value;
    }
    
    public GrantedPrivilegeEntity(String value, boolean system) {
        this.value = value;
        this.system = system;
    }
    
    public GrantedPrivilegeEntity(String value,boolean read, boolean write) {
        this(value);
        this.read = read;
        this.write = write;
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

    /**
     * @return the role
     */
    public RoleEntity getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(RoleEntity role) {
        this.role = role;
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
        final GrantedPrivilegeEntity other = (GrantedPrivilegeEntity) obj;
        
        
        if ((this.getId() == null || other.getId() == null)) {
            return (this.getValue().equals(other.getValue()) && this.read == other.read && this.write == other.write);
        }
        return (this.getId() != null && !this.getId().equals(other.getId()));
    }

    @Override
    public String toString() {
        return "com.encooked.data.entities.GrantedPrivilegeEntity[ id=" + getId() + " ]";
    }

}
