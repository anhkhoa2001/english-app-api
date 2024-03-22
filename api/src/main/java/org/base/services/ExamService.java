package org.base.services;

import org.base.dto.exam.ExamDTO;
import org.base.dto.exam.ExamRequest;
import org.base.dto.exam.ExamSubmitDTO;
import org.base.model.exam.ExamModel;

public interface ExamService {

    ExamModel create(ExamDTO request);

    Object getAll();

    ExamModel getByCode(String code);

    ExamModel update(ExamDTO request);

    void delete(String examCode);

    void deletePart(String examCode, int partId);

    Object getAllExam(ExamRequest examRequest);

    Object toExamine(ExamSubmitDTO request);
}
