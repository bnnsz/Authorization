/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.components;

import com.encooked.entities.AbstractEntity;
import com.encooked.entities.AuditLogEntity;
import com.encooked.entities.enums.AuditLogAction;
import static com.encooked.entities.enums.AuditLogAction.*;
import com.encooked.repositories.AuditLogEntityRepository;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 *
 * @author ObinnaAsuzu
 */
public class EntityListener {

    @Autowired
    AuditLogEntityRepository repository;
    

    

    @PrePersist
    public void prePersist(AbstractEntity item) {
        item.setCreatedTimestamp(LocalDateTime.now());
        item.setUpdatedTimestamp(LocalDateTime.now());
        audit(item, CREATED);
    }

    @PreUpdate
    public void preUpdate(AbstractEntity item) {
        item.setUpdatedTimestamp(LocalDateTime.now());
        audit(item, UPDATED);
    }

    @PreRemove
    public void preRemove(AbstractEntity item) {
        audit(item, REMOVED);
    }

    public void audit(AbstractEntity item, AuditLogAction action) {
        AutowireHelper.autowire(this, this.repository);
        AuditLogEntity log = new AuditLogEntity();
        log.setTimestamp(LocalDateTime.now());
        log.setEntityRef(String.valueOf(item.getId()));
        log.setEntityName(item.getClass().getSimpleName());
        log.setAction(action);
        try {
            log.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        } catch (Exception e) {
            log.setUsername("anonymous");
        }
        repository.save(log);
    }
}
