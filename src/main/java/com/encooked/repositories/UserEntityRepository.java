/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.UserEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author obinna.asuzu
 */
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByUsername(String username);
    
    @Query("SELECT DISTINCT a FROM UserEntity a JOIN a.principals p "
            + "WHERE (a.username LIKE %:criteria% "
            + "OR p.value LIKE %:criteria%) "
            + "AND (a.createdTimestamp BETWEEN :fromDate AND :toDate)")
    public Page<UserEntity> searchByCriteria(
            @Param("criteria") String criteria,
            @Param("fromDate") LocalDateTime fromDate,
            @Param("toDate") LocalDateTime toDate,
            Pageable pageable);
}
