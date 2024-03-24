package org.base.dao;

import org.base.dto.exam.ExamHistoryDTO;

public interface ExamHistoryDao {

    Object getByCondition(ExamHistoryDTO request);
}
