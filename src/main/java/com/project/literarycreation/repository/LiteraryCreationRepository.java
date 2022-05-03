package com.project.literarycreation.repository;

import com.project.literarycreation.domain.LiteraryCreation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiteraryCreationRepository extends JpaRepository<LiteraryCreation, Integer> {

    List<LiteraryCreation> findAllByAuthorId(Long userId);

    List<LiteraryCreation> findAllByAuthorIdAndStatusId(Long userId, Integer statusId);

    List<LiteraryCreation> findAllByStatusIdIn(List<Integer> statusId);

    Integer countByStatusId(Integer statusId);
}
