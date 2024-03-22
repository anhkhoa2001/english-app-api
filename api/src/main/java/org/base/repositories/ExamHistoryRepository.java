package org.base.repositories;

import org.base.model.exam.ExamHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamHistoryRepository extends JpaRepository<ExamHistoryModel, Integer> {
}
