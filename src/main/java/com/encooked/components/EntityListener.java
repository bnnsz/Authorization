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
import com.google.gson.Gson;
import java.time.LocalDateTime;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 *
 * @author ObinnaAsuzu
 */
@Component
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
        AuditLogEntity log = new AuditLogEntity();
        Gson gson = new Gson();
        log.setEntityState(gson.toJson(item));
        log.setTimestamp(LocalDateTime.now());
        log.setEntityRef(String.valueOf(item.getId()));
        log.setEntityName(item.getClass().getSimpleName());
        log.setAction(action);
        log.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        repository.save(log);
    }
}
