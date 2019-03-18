/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.TokenEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author obinna.asuzu
 */
public interface TokenEntityRepository extends JpaRepository<TokenEntity, Long> {
    public Optional<TokenEntity> findByValue(String value);
}
