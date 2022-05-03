package com.project.literarycreation.repository;

import com.project.literarycreation.domain.Group;
import com.project.literarycreation.domain.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
	List<Group> findByAdmin(User admin);
}
