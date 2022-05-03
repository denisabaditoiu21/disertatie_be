package com.project.literarycreation.repository;


import com.project.literarycreation.Utils.RoleName;
import com.project.literarycreation.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findByName(RoleName roleName);

}