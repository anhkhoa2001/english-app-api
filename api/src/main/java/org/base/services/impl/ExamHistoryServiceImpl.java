package org.base.services.impl;

import org.base.config.JwtTokenSetup;
import org.base.dto.exam.ExamHistoryDTO;
import org.base.repositories.ExamHistoryRepository;
import org.base.services.ExamHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExamHistoryServiceImpl implements ExamHistoryService {

    private final ExamHistoryRepository examHistoryRepository;
    private final JwtTokenSetup jwtTokenSetup;

    @Autowired
    public ExamHistoryServiceImpl(ExamHistoryRepository examHistoryRepository,
                                  JwtTokenSetup jwtTokenSetup) {
        this.examHistoryRepository = examHistoryRepository;
        this.jwtTokenSetup = jwtTokenSetup;
    }

    @Override
    public Object getAll(String token, String examCode, Integer page, Integer pageSize) {
        String userId = jwtTokenSetup.getUserIdFromToken(token);
        return examHistoryRepository.findAllByImplementerAndExamCode(userId, examCode);
    }

    @Override
    public Object getByCondition(ExamHistoryDTO request) {
        return examHistoryRepository.getByCondition(request);
    }

    @Override
    public Object getByHistoryId(Integer historyId) {
        return examHistoryRepository.findById(historyId).get();
    }
}
