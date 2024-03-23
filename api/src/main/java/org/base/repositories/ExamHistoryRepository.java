package org.base.repositories;

import org.base.model.exam.ExamHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamHistoryRepository extends JpaRepository<ExamHistoryModel, Integer> {

    @Query(value = "select exam from ExamHistoryModel exam where exam.implementer = :implementer ORDER BY exam.createTime desc")
    List<ExamHistoryModel> findAllByImplementerAnd(String implementer);
}
