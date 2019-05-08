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
public class UserPrincipalEntity implements Serializable {

    @ManyToOne
    private UserEntity user;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name="principal_key")
    private String key;
    @Column(name="principal_value")
    private String value;

    public UserPrincipalEntity() {
    }
    
    

    public UserPrincipalEntity(UserEntity user, Long id, String key, String value) {
        this.user = user;
        this.id = id;
        this.key = key;
        this.value = value;
    }
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserPrincipalEntity)) {
            return false;
        }
        UserPrincipalEntity other = (UserPrincipalEntity) object;
        
        if(this.id == null){
            if(this.key == null){
                return false;
            }else{
                return this.key.equals(other.key);
            }
        }
        
        if(other.id == null){
            if(other.key == null){
                return false;
            }else{
                return this.key.equals(other.key);
            }
        }
        
        return this.id.equals(other.id);
    }

    @Override
    public String toString() {
        return "com.demo.entities.UserPrincipaEntity[ id=" + id + " ]";
    }
    
}
