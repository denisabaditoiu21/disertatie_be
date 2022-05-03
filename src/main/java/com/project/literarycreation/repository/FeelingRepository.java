package com.project.literarycreation.repository;

import com.project.literarycreation.domain.Feeling;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeelingRepository extends JpaRepository<Feeling, Integer> {
}
