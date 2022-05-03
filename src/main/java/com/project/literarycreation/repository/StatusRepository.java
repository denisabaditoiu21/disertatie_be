package com.project.literarycreation.repository;

import com.project.literarycreation.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {
}
