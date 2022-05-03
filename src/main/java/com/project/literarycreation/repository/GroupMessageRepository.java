package com.project.literarycreation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.literarycreation.domain.GroupMessage;

public interface GroupMessageRepository extends JpaRepository<GroupMessage, Integer> {

	List<GroupMessage> findByGroupIdOrderByCreationDateAsc(Integer groupId);

}
