/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.RoleEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author obinna.asuzu
 */
public interface RoleEntityRepository extends JpaRepository<RoleEntity, Long> {

    public Optional<RoleEntity> findByName(String name);

    public List<RoleEntity> deleteByName(String name);
}
