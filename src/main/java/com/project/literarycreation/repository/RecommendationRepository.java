package com.project.literarycreation.repository;

import com.project.literarycreation.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {

    List<Recommendation> findAllByReceiverIdAndAcceptedNot(long receiverId, int accepted);
}
