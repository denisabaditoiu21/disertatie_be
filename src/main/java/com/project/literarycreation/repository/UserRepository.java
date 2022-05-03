package com.project.literarycreation.repository;

import com.project.literarycreation.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByRolesIdIn(List<Integer> roles);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
