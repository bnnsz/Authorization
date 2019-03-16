/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.GrantedPriviledgeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author obinna.asuzu
 */
public interface GrantedPriviledgeEntityRepository extends JpaRepository<GrantedPriviledgeEntity, Long> {
    public Optional<GrantedPriviledgeEntity> findByValue(String value);
}
