package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dao.FileDao;
import org.base.dto.lesson.LessonDTO;
import org.base.exception.AppException;
import org.base.exception.SystemException;
import org.base.model.course.LessonModel;
import org.base.model.course.SectionModel;
import org.base.repositories.LessonRepository;
import org.base.repositories.SectionRepository;
import org.base.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private FileDao fileDao;

    @Override
    public LessonModel create(LessonDTO dto) {
        try {
            Optional<SectionModel> opSec = sectionRepository.findById(dto.getSection_id());
            LessonModel lessonExist = lessonRepository.getByLessionNameAndSectionR(dto.getLessonName(), dto.getSection_id());
            if(opSec.isEmpty()) {
                throw new SystemException("section code not exist!!!");
            }

            if(lessonExist != null) {
                throw new SystemException("Lesson already exist!!");
            }

            LessonModel lessonModel = new LessonModel();
            lessonModel.setDescription(dto.getDes());
            lessonModel.setLessionName(dto.getLessonName());
            lessonModel.setStatus(dto.isStatus());
            lessonModel.setThumbnail(fileDao.getUrlInFile(dto.getThumbnail()));
            lessonModel.setUrl_video(fileDao.getUrlInFile(dto.getVideo()));
            lessonModel.setCreateAt(new Date());
            lessonModel.setSectionModel(opSec.get());

            lessonModel = lessonRepository.save(lessonModel);
            log.info(lessonModel.toString());
            return lessonModel;
        } catch (Exception e) {
            log.error("Occur error when create lesson!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());
            e.printStackTrace();
            throw new AppException("create section failed!!!");
        }
    }

    @Override
    public LessonModel getByLessonId(Integer lessonId) {
        try {
            return lessonRepository.findById(lessonId).get();
        } catch (Exception e) {
            log.error("Occur error when get lesson!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new AppException("create lesson failed!!!");
        }
    }

    @Override
    @Transactional
    public void deleteByLessonId(Integer lessonId) {
        try {
            List<Integer> ids = new ArrayList<>();
            ids.add(lessonId);

            lessonRepository.deleteByListId(ids);
        } catch (Exception e) {
            log.error("Occur error when delete lesson by id!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new AppException("delete lesson failed!!!");
        }
    }

    @Override
    public LessonModel update(LessonDTO dto) {
        try {
            Optional<SectionModel> opSec = sectionRepository.findById(dto.getSection_id());
            if(opSec.isEmpty()) {
                throw new SystemException("lesson code not exist!!!");
            }
            LessonModel lessonModel = lessonRepository.getByLessionNameAndSectionR(dto.getLessonName(), dto.getSection_id());
            if(lessonModel == null) {
                throw new SystemException("Lesson already not exist!!");
            }

            lessonModel.setDescription(dto.getDes());
            lessonModel.setLessionName(dto.getLessonName());
            lessonModel.setStatus(dto.isStatus());
            lessonModel.setThumbnail(fileDao.getUrlInFile(dto.getThumbnail()));
            lessonModel.setUrl_video(fileDao.getUrlInFile(dto.getVideo()));
            lessonModel.setSectionModel(opSec.get());

            lessonModel = lessonRepository.save(lessonModel);

            return lessonModel;
        } catch (Exception e) {
            log.error("Occur error when update lesson!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new SystemException(e.getMessage());
        }
    }
}
