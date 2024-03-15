package org.base.repositories;

import org.base.model.exam.ExamModel;
import org.base.model.exam.ExamPartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamPartRepository extends JpaRepository<ExamPartModel, Integer> {

    ExamPartModel getByPartIdAndExamModel(int partId, ExamModel examModel);
}
