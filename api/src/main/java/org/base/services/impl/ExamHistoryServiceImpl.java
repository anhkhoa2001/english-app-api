package org.base.services.impl;

import org.base.config.JwtTokenSetup;
import org.base.repositories.ExamHistoryRepository;
import org.base.services.ExamHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public Object getAll(String token, Integer page, Integer pageSize) {
        String userId = jwtTokenSetup.getUserIdFromToken(token);
        return examHistoryRepository.findAllByImplementerAnd(userId);
    }
}
