package com.project.literarycreation.repository;

import com.project.literarycreation.domain.Paragraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParagraphRepository extends JpaRepository<Paragraph, Long> {

    List<Paragraph> findAllByFeelingId(Integer feelingId);
}
