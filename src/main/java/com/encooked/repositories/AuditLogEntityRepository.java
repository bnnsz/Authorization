/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.AuditLogEntity;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author obinna.asuzu
 */
public interface AuditLogEntityRepository extends CrudRepository<AuditLogEntity, Long> {
    
}
