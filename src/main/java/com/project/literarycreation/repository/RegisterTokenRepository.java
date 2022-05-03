package com.project.literarycreation.repository;

import com.project.literarycreation.domain.RegisterToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RegisterTokenRepository extends CrudRepository<RegisterToken, Long> {
    Optional<RegisterToken> findByToken(String token);
    Optional<RegisterToken> findByEmail(String email);
}
