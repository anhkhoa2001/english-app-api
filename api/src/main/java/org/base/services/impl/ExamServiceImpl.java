package org.base.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.base.config.JwtTokenSetup;
import org.base.dto.exam.ExamDTO;
import org.base.dto.exam.ExamRequest;
import org.base.dto.exam.ExamSubmitDTO;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.exam.*;
import org.base.repositories.ExamHistoryRepository;
import org.base.repositories.ExamPartRepository;
import org.base.repositories.ExamRepository;
import org.base.services.ExamService;
import org.base.utils.Constants;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;

    private final ExamPartRepository examPartRepository;

    private final JwtTokenSetup jwtTokenSetup;

    private final ObjectMapper objectMapper;

    private final ExamHistoryRepository examHistoryRepository;

    @Autowired
    public ExamServiceImpl(ExamRepository examRepository,
                           ExamPartRepository examPartRepository,
                           JwtTokenSetup jwtTokenSetup,
                           ObjectMapper objectMapper,
                           ExamHistoryRepository examHistoryRepository) {
        this.examRepository = examRepository;
        this.examPartRepository = examPartRepository;
        this.jwtTokenSetup = jwtTokenSetup;
        this.objectMapper = objectMapper;
        this.examHistoryRepository = examHistoryRepository;
    }

    @Override
    public ExamModel create(ExamDTO request, String token) {
        try {
            String userCreate = jwtTokenSetup.getUserIdFromToken(token);
            Optional<ExamModel> op = examRepository.findById(request.getExamCode());

            if(op.isPresent()) {
                throw new ValidationException("exam code is already exist!!!");
            }

            ExamModel examModel = new ExamModel();
            examModel.setExamCode(request.getExamCode());
            examModel.setExamName(request.getExamName());
            examModel.setSkill(request.getSkill());
            examModel.setSummary(request.getSummary());
            examModel.setDescription(request.getDescription());
            examModel.setType(request.getType());
            examModel.setCreateAt(new Date());
            examModel.setStatus(request.isStatus());
            examModel.setCountdown(request.getCountdown());
            examModel.setTotalQuestion(0);
            examModel.setCreateBy(userCreate);
            if(!StringUtil.isListEmpty(request.getThumbnail())) {
                String image = (String) request.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(request.getThumbnail().get(0).getType().contains("image/")) {
                    examModel.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            examModel = examRepository.save(examModel);

            return examModel;
        } catch (Exception e) {
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public Object getAll() {
        return examRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }

    @Override
    public ExamModel getByCode(String code) {
        try {
            ExamModel examModel = examRepository.findById(code).get();
            examModel.getParts().forEach(part -> {
                part.getQuestions().sort(new Comparator<QuestionModel>() {
                    public int compare(QuestionModel o1, QuestionModel o2) {
                        return Integer.compare(o1.getQuestionId(), o2.getQuestionId());
                    }
                });
            });
            return examModel;
        } catch (Exception e) {
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public ExamModel update(ExamDTO request) {
        try {
            Optional<ExamModel> op = examRepository.findById(request.getExamCode());

            if(op.isEmpty()) {
                throw new ValidationException("exam code is not exist!!!");
            }

            ExamModel examModel = op.get();
            examModel.setExamName(request.getExamName());
            examModel.setSkill(request.getSkill());
            examModel.setSummary(request.getSummary());
            examModel.setDescription(request.getDescription());
            examModel.setType(request.getType());
            examModel.setCreateAt(new Date());
            examModel.setStatus(request.isStatus());
            examModel.setCountdown(request.getCountdown());
            if(!StringUtil.isListEmpty(request.getThumbnail())) {
                String image = (String) request.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(request.getThumbnail().get(0).getType().contains("image/")) {
                    examModel.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            examModel = examRepository.save(examModel);

            return examModel;
        } catch (Exception e) {
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public void delete(String examCode) {
        try {
            Optional<ExamModel> op = examRepository.findById(examCode);

            if(op.isEmpty()) {
                throw new ValidationException("exam code is not exist!!!");
            }

            ExamModel examModel = op.get();
            examModel.setStatus(false);

            examRepository.save(examModel);
        } catch (Exception e) {
            log.error("{} | {}", e.getClass(), e.getMessage());
            throw new SystemException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deletePart(String examCode, int partId) {
        Optional<ExamModel> opExam = examRepository.findById(examCode);

        if(opExam.isEmpty()) {
            throw new ValidationException("exam code is not exist!!!");
        }

        ExamPartModel examPartModel = examPartRepository.getByPartIdAndExamModel(partId, opExam.get());
        examPartRepository.delete(examPartModel);
    }

    @Override
    public Object getAllExam(ExamRequest examRequest) {
        return examRepository.getAllExam(examRequest);
    }

    @Override
    public void toExamine(ExamSubmitDTO request, String token) {
        try {
            String userCreate = jwtTokenSetup.getUserIdFromToken(token);
            Optional<ExamModel> opExam = examRepository.findById(request.getExamCode());

            if(opExam.isEmpty()) {
                throw new ValidationException("exam code is not exist!!!");
            }
            ExamHistoryModel his = new ExamHistoryModel();
            List<QuestionItemModel> questions = new ArrayList<>();
            ExamModel exam = request.getExam();
            for(ExamPartModel part : exam.getParts()) {
                for(QuestionModel ques:part.getQuestions()) {
                    questions.addAll(ques.getQuestionChilds());
                }
            }

            his.setImplementer(userCreate);
            his.setCreateTime(new Date());
            his.setExecuteTime(request.getExecuteTime());
            his.setExamCode(request.getExamCode());
            his.setJson(objectMapper.writeValueAsString(request.getExam()));

            int countTrue = 0;
            for(QuestionItemModel question:questions) {
                if(question.getOutput() != null) {
                    String solution = question.getSolution().trim().toLowerCase();
                    String output = question.getOutput().trim().toLowerCase();

                    countTrue = solution.equals(output) ? ++countTrue : countTrue;
                }
            }
            his.setResult(countTrue + "/" + exam.getTotalQuestion());

            examHistoryRepository.save(his);

            return;
        } catch (Exception e) {
            log.error("examine failed {} {}", e.getClass(), e.getMessage());
            e.printStackTrace();
            throw new SystemException(e.getCause().toString());
        }
    }
}
