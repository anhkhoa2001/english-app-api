package org.base.repositories;

import org.base.model.exam.ExamPartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPartRepository extends JpaRepository<ExamPartModel, Integer> {
}
