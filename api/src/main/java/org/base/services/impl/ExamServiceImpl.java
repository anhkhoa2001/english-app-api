package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dto.exam.ExamDTO;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.exam.ExamModel;
import org.base.repositories.ExamRepository;
import org.base.services.ExamService;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class ExamServiceImpl implements ExamService {

    @Autowired
    private ExamRepository examRepository;

    @Override
    public ExamModel create(ExamDTO request) {
        try {
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
            return examRepository.findById(code).get();
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
    public ExamModel deletePart(String examCode, int partId) {
        return null;
    }
}
