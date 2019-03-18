/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.PrivilegeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author obinna.asuzu
 */
public interface PrivilegeEntityRepository extends JpaRepository<PrivilegeEntity, Long> {
    public Optional<PrivilegeEntity> findByValue(String value);

    public List<PrivilegeEntity> deleteByValue(String value);
    
}
