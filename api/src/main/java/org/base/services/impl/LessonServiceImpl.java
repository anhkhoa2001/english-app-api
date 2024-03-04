package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.exception.AppException;
import org.base.model.LessonModel;
import org.base.model.SectionModel;
import org.base.repositories.LessonRepository;
import org.base.repositories.SectionRepository;
import org.base.services.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class LessonServiceImpl implements LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private SectionRepository sectionRepository;


    @Override
    public LessonModel create(LessonModel lessonModel) {
        try {
            SectionModel sectionModel = sectionRepository.findById(lessonModel.getSection_id()).get();

            lessonModel.setCreateAt(new Date());
            lessonModel.setSectionModel(sectionModel);

            lessonModel = lessonRepository.save(lessonModel);
            return lessonModel;
        } catch (Exception e) {
            log.error("Occur error when create section!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new AppException("create section failed!!!");
        }
    }
}
