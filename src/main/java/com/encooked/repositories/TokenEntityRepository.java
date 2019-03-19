/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.encooked.repositories;

import com.encooked.entities.TokenEntity;
import com.encooked.entities.UserEntity;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author obinna.asuzu
 */
public interface TokenEntityRepository extends JpaRepository<TokenEntity, Long> {

    public Optional<TokenEntity> findByValue(String value);

    public Stream<TokenEntity> findByUser_Username(String username);

    public Stream<TokenEntity> findByUser_Id(Long id);

    public Stream<TokenEntity> findByUser(UserEntity user);

    public Stream<TokenEntity> findByUserAndExpired(UserEntity user, boolean expired);
}
