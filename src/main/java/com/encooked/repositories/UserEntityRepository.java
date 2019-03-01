/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 *
 * @author obinna.asuzu
 */
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByUsername(String username);
    
    @Query("SELECT u FROM User u WHERE u.status = 1")
    public Optional<UserEntity> findByRole(String username);
}
