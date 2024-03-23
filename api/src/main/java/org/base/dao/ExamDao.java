package org.base.dao;

import org.base.dto.exam.ExamRequest;
import org.springframework.stereotype.Repository;

public interface ExamDao {

    Object getAllExam(ExamRequest request);

    Object getExamByCondition(ExamRequest examRequest);
}
