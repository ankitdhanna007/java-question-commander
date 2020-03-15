package com.cgm.project.data.repositories;

import com.cgm.project.data.entities.QuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {

    Optional<QuestionEntity> findByQuestion(String questionText);

}
