package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dto.exam.QuestionDTO;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.exam.*;
import org.base.repositories.ExamPartRepository;
import org.base.repositories.ExamRepository;
import org.base.repositories.QuestionRepository;
import org.base.services.QuestionService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamPartRepository examPartRepository;

    @Override
    public QuestionModel create(QuestionDTO request) {
        try {
            Optional<ExamModel> opExam = examRepository.findById(request.getExamCode());
            if(opExam.isEmpty()) {
                throw new ValidationException("exam code does not exist!!!!");
            }
            List<ExamPartModel> examParts = opExam.get().getParts();
            examParts = examParts.stream().filter(e -> e.getPartId().equals(request.getPart())).collect(Collectors.toList());

            ExamPartModel examPartUseful = null;
            if(examParts.isEmpty()) {
                examPartUseful = new ExamPartModel();
                examPartUseful.setPartId(request.getPart());
                examPartUseful.setExamModel(opExam.get());

                examPartUseful = examPartRepository.save(examPartUseful);
            } else {
                examPartUseful = examParts.get(0);
            }

            QuestionModel questionModel = new QuestionModel();
            questionModel.setPartModel(examPartUseful);
            questionModel.setContent(request.getContent());
            questionModel.setType(request.getType());

            List<QuestionItemModel> questionItems = request.getQuestions().stream().map(q -> {
                QuestionItemModel questionItem = new QuestionItemModel();
                questionItem.setHint(q.getHint());
                if(q.getType().contains(Constants.TYPE_QUESTION.AUDIO)) {
                    questionItem.setContent(q.getContent());
                } else if(q.getType().contains(Constants.TYPE_QUESTION.NO_CONTENT)) {
                    questionItem.setContent(null);
                } else {
                    questionItem.setContent(q.getContent());
                }

                if(q.getType().contains(Constants.TYPE_QUESTION.MULTI_CHOICE)) {
                    List<AnswerAttributeModel> attributes = q.getAnswer().stream().map(a -> {
                        AnswerAttributeModel ansModel = new AnswerAttributeModel();
                        ansModel.setQuestionItemModel(questionItem);
                        ansModel.setKey(a.getKey());
                        ansModel.setValue(a.getValue());

                        return ansModel;
                    }).collect(Collectors.toList());
                }
                questionItem.setSolution(q.getSolution());

                return questionItem;
            }).collect(Collectors.toList());
            questionModel.setQuestionChilds(questionItems);

            return null;
        } catch (Exception e) {
            log.error("add question failed {} {}", e.getClass(), e.getMessage());
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }
}
