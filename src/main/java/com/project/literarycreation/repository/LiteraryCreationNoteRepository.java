package com.project.literarycreation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.literarycreation.domain.LiteraryCreation;
import com.project.literarycreation.domain.LiteraryCreationNote;
import com.project.literarycreation.domain.User;

public interface LiteraryCreationNoteRepository extends JpaRepository<LiteraryCreationNote, Integer> {

	boolean existsByLiteraryCreationAndUser(LiteraryCreation creation, User user);
	
	@Query(value = "SELECT AVG(note) FROM LiteraryCreationNote where literary_creation_id =:creationId")
	public Double getAvg(@Param("creationId") Integer creationId);
}
