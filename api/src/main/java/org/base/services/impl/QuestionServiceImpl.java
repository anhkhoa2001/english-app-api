package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dto.exam.QuestionDTO;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.exam.*;
import org.base.repositories.*;
import org.base.services.QuestionService;
import org.base.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
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
    private QuestionItemRepository questionItemRepository;

    @Autowired
    private ExamPartRepository examPartRepository;

    @Autowired
    private AnswerAttributeRepository answerAttributeRepository;

    @Override
    @Transactional
    public QuestionModel create(QuestionDTO request) {
        Optional<ExamModel> opExam = examRepository.findById(request.getExamCode());
        if(opExam.isEmpty()) {
            throw new ValidationException("exam code does not exist!!!!");
        }
        ExamModel examModel = opExam.get();
        List<ExamPartModel> examParts = examModel.getParts();
        examParts = examParts.stream().filter(e -> e.getPartId().equals(request.getPart())).collect(Collectors.toList());

        ExamPartModel examPartUseful = null;
        if(examParts.isEmpty()) {
            examPartUseful = new ExamPartModel();
            examPartUseful.setPartId(request.getPart());
            examPartUseful.setPartPoint(request.getPartPoint());
            examPartUseful.setExamModel(opExam.get());

            examPartUseful = examPartRepository.save(examPartUseful);
        } else {
            examPartUseful = examParts.get(0);
        }

        QuestionModel questionModel = new QuestionModel();
        questionModel.setPartModel(examPartUseful);
        questionModel.setContent(request.getContent());
        questionModel.setType(request.getType());

        questionModel = questionRepository.save(questionModel);
        QuestionModel finalQuestionModel = questionModel;
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
            questionItem.setSolution(q.getSolution());
            questionItem.setType(q.getType());
            questionItem.setQuestionModel(finalQuestionModel);

            questionItem = questionItemRepository.save(questionItem);
            if(q.getType().contains(Constants.TYPE_QUESTION.MULTI_CHOICE)) {
                QuestionItemModel finalQuestionItem = questionItem;
                List<AnswerAttributeModel> attributes = q.getAnswer().stream().map(a -> {
                    AnswerAttributeModel ansModel = new AnswerAttributeModel();
                    ansModel.setQuestionItemModel(finalQuestionItem);
                    ansModel.setKey(a.getKey());
                    ansModel.setValue(a.getValue());

                    ansModel = answerAttributeRepository.save(ansModel);
                    return ansModel;
                }).collect(Collectors.toList());
                questionItem.setAnswer(attributes);
            }

            return questionItem;
        }).collect(Collectors.toList());

        //update total question
        examModel.setTotalQuestion(examModel.getTotalQuestion() + questionItems.size());
        examRepository.save(examModel);

        //update question
        questionModel.setQuestionChilds(questionItems);
        return questionModel;
    }

    @Override
    @Transactional
    public QuestionModel update(QuestionDTO request) {
        Optional<ExamModel> opExam = examRepository.findById(request.getExamCode());
        if(opExam.isEmpty()) {
            throw new ValidationException("exam code does not exist!!!!");
        }
        ExamModel examModel = opExam.get();
        List<ExamPartModel> examParts = opExam.get().getParts();
        examParts = examParts.stream().filter(e -> e.getPartId().equals(request.getPart())).collect(Collectors.toList());

        ExamPartModel examPartUseful = null;
        if(examParts.isEmpty()) {
            examPartUseful = new ExamPartModel();
            examPartUseful.setPartId(request.getPart());
            examPartUseful.setPartPoint(request.getPartPoint());
            examPartUseful.setExamModel(opExam.get());

            examPartUseful = examPartRepository.save(examPartUseful);
        } else {
            examPartUseful = examParts.get(0);
        }
        Optional<QuestionModel> opQues = questionRepository.findById(request.getQuestionId());

        if(opQues.isEmpty()) {
            throw new ValidationException("question not found!!!");
        }

        QuestionModel questionModel = opQues.get();
        questionModel.setPartModel(examPartUseful);
        questionModel.setContent(request.getContent());
        questionModel.setType(request.getType());

        List<Integer> questionItemOld = questionModel.getQuestionChilds().stream().map(QuestionItemModel::getItem_id).collect(Collectors.toList());
        questionItemRepository.deleteAllById(questionItemOld);

        QuestionModel finalQuestionModel = questionModel;
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
            questionItem.setSolution(q.getSolution());
            questionItem.setType(q.getType());
            questionItem.setQuestionModel(finalQuestionModel);
            if(q.getType().contains(Constants.TYPE_QUESTION.MULTI_CHOICE)) {
                QuestionItemModel finalQuestionItem = questionItem;
                List<AnswerAttributeModel> attributes = q.getAnswer().stream().map(a -> {
                    AnswerAttributeModel ansModel = new AnswerAttributeModel();
                    ansModel.setQuestionItemModel(finalQuestionItem);
                    ansModel.setKey(a.getKey());
                    ansModel.setValue(a.getValue());

                    ansModel = answerAttributeRepository.save(ansModel);
                    return ansModel;
                }).collect(Collectors.toList());
                questionItem.setAnswer(attributes);
            }

            return questionItem;
        }).collect(Collectors.toList());

        examModel.setTotalQuestion(examModel.getTotalQuestion() - questionItemOld.size() + questionItems.size());
        examRepository.save(examModel);

        questionModel.setQuestionChilds(questionItems);
        questionModel = questionRepository.save(questionModel);
        return questionModel;
    }

    @Override
    public List<QuestionModel> getAll() {
        return questionRepository.findAll(Sort.by(Sort.Direction.ASC, "questionId"));
    }

    @Override
    @Transactional
    public void deleteQuestion(Integer questionId) {
        Optional<QuestionModel> op = questionRepository.findById(questionId);

        if(op.isEmpty()) {
            throw new ValidationException("Question not found!!!");
        }

        QuestionModel questionModel = op.get();
        questionRepository.delete(questionModel);
    }
}
