package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dao.FileDao;
import org.base.dto.lesson.LessonDTO;
import org.base.exception.AppException;
import org.base.exception.SystemException;
import org.base.model.course.CourseModel;
import org.base.model.course.LessonModel;
import org.base.model.course.SectionModel;
import org.base.model.exam.ExamPartModel;
import org.base.repositories.*;
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
    private CourseRepository courseRepository;

    @Autowired
    private ExamPartRepository examPartRepository;

    @Autowired
    private FileDao fileDao;

    @Override
    public LessonModel create(LessonDTO dto) {
        try {
            Optional<SectionModel> opSec = sectionRepository.findById(dto.getSectionId());
            LessonModel lessonExist = lessonRepository.getByLessionNameAndSectionR(dto.getLessonName(), dto.getSectionId());
            if(opSec.isEmpty()) {
                throw new SystemException("section code not exist!!!");
            }

            Optional<CourseModel> opCourse = courseRepository.findById(opSec.get().getCourseModel().getCode());

            if(opCourse.isEmpty()) {
                throw new SystemException("course code not exist!!!");
            }

            if(lessonExist != null) {
                throw new SystemException("Lesson already exist!!");
            }

            LessonModel lessonModel = new LessonModel();
            lessonModel.setType(dto.getType());
            if(dto.getType().equals("Minitest")) {
                ExamPartModel examPartModel = examPartRepository.findById(dto.getExamPartId()).orElseGet(null);
                lessonModel.setExamModel(examPartModel);
            } else {
                lessonModel.setStatus(dto.isStatus());
                lessonModel.setThumbnail(fileDao.getUrlInFile(dto.getThumbnail()));
                lessonModel.setUrl_video(fileDao.getUrlInFile(dto.getVideo()));
            }
            lessonModel.setDescription(dto.getDescription());
            lessonModel.setCreateAt(new Date());
            lessonModel.setSectionModel(opSec.get());
            lessonModel.setLessionName(dto.getLessonName());

            lessonModel = lessonRepository.save(lessonModel);
            CourseModel courseModel = opCourse.get();
            courseModel.setLectures(courseModel.getLectures() + 1);

            courseRepository.save(courseModel);

            return lessonModel;
        } catch (Exception e) {
            log.error("Occur error when create lesson!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());
            e.printStackTrace();
            throw new SystemException("create section failed!!!");
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
            Optional<SectionModel> opSec = sectionRepository.findById(dto.getSectionId());
            if(opSec.isEmpty()) {
                throw new SystemException("lesson code not exist!!!");
            }
            Optional<LessonModel> opLes = lessonRepository.findById(dto.getLessonId());
            if(opLes.isEmpty()) {
                throw new SystemException("Lesson already not exist!!");
            }

            LessonModel lessonModel = opLes.get();

            if(dto.getType().equals("Minitest")) {
                ExamPartModel examPartModel = examPartRepository.findById(dto.getExamPartId()).orElseGet(null);
                lessonModel.setExamModel(examPartModel);
            } else {
                lessonModel.setStatus(dto.isStatus());
                lessonModel.setThumbnail(fileDao.getUrlInFile(dto.getThumbnail()));
                lessonModel.setUrl_video(fileDao.getUrlInFile(dto.getVideo()));
            }
            lessonModel.setDescription(dto.getDescription());
            lessonModel.setCreateAt(new Date());
            lessonModel.setSectionModel(opSec.get());
            lessonModel.setLessionName(dto.getLessonName());

            lessonModel = lessonRepository.save(lessonModel);

            return lessonModel;
        } catch (Exception e) {
            log.error("Occur error when update lesson!!!");
            log.error("{} | {}", e.getClass(), e.getMessage());
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }
}
