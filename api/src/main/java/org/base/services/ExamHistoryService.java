package org.base.services;

import org.base.dto.exam.ExamHistoryDTO;

public interface ExamHistoryService {

    Object getAll(String token, String examCode, Integer page, Integer pageSize);

    Object getByCondition(ExamHistoryDTO request);

    Object getByHistoryId(Integer historyId);
}
