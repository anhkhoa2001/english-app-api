package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dao.FileDao;
import org.base.dto.lesson.LessonDTO;
import org.base.exception.AppException;
import org.base.exception.SystemException;
import org.base.model.CourseModel;
import org.base.model.LessonModel;
import org.base.model.SectionModel;
import org.base.repositories.LessonRepository;
import org.base.repositories.SectionRepository;
import org.base.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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

            throw new AppException("create section failed!!!");
        }
    }
}
