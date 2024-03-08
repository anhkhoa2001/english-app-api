package org.base.repositories;

import org.base.model.exam.AnswerAttributeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerAttributeRepository extends JpaRepository<AnswerAttributeModel, Integer> {
}
