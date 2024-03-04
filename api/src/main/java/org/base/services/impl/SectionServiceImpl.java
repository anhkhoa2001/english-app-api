package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.exception.AppException;
import org.base.exception.SystemException;
import org.base.model.CourseModel;
import org.base.model.SectionModel;
import org.base.repositories.CourseRepository;
import org.base.repositories.SectionRepository;
import org.base.services.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class SectionServiceImpl implements SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public SectionModel create(SectionModel sectionModel) {
        try {
            Optional<CourseModel> opCourse = courseRepository.findById(sectionModel.getCourseCode());
            SectionModel sectionExist = sectionRepository.getBySectionName(sectionModel.getSectionName());
            if(opCourse.isEmpty()) {
                throw new SystemException("course code not exist!!!");
            }

            if(sectionExist != null) {
                throw new SystemException("Section already exist!!");
            }
            CourseModel courseModel = opCourse.get();
            sectionModel.setCourseModel(courseModel);

            sectionModel = sectionRepository.save(sectionModel);

            return sectionModel;
        } catch (Exception e) {
            log.error("Occur error when create section!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());

            throw new SystemException(e.getMessage());
        }
    }
}
