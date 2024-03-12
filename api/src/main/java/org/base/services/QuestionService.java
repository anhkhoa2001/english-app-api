package org.base.services;

import org.base.dto.exam.QuestionDTO;
import org.base.model.exam.QuestionModel;
import org.springframework.stereotype.Service;

public interface QuestionService {

    QuestionModel create(QuestionDTO request);
}
