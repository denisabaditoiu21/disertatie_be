package com.project.literarycreation.repository;

import com.project.literarycreation.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
    Genre findFirstById(Integer id);
}
