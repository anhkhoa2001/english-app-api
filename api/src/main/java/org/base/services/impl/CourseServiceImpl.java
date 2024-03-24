package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.config.JwtTokenSetup;
import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.course.CourseModel;
import org.base.model.course.LessonModel;
import org.base.model.course.StudentOfCourseModel;
import org.base.repositories.CourseRepository;
import org.base.repositories.StudentOfCourseRepository;
import org.base.services.CourseService;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private JwtTokenSetup jwtTokenSetup;

    @Autowired
    private StudentOfCourseRepository studentOfCourseRepository;

    @Override
    public CourseModel create(CourseItemDTO item, String token) {
        try {
            String userId = jwtTokenSetup.getUserIdFromToken(token);
            Optional<CourseModel> op = courseRepository.findById(item.getCourseCode());

            if(op.isPresent()) {
                throw new ValidationException("Course already exist!!!");
            }

            CourseModel courseModel = new CourseModel();
            courseModel.setCode(item.getCourseCode());
            courseModel.setCourseName(item.getCourseName());
            courseModel.setDescription(item.getDescription());
            courseModel.setCreateAt(new Date());
            courseModel.setCreateBy(userId);
            courseModel.setStatus(item.isStatus());
            courseModel.setPublic(item.isPublic());
            courseModel.setLevel(item.getLevel());
            courseModel.setSummary(item.getSummary());

            if(!StringUtil.isListEmpty(item.getThumbnail())) {
                String image = (String) item.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(item.getThumbnail().get(0).getType().contains("image/")) {
                    courseModel.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            courseModel = courseRepository.save(courseModel);
            return courseModel;
        } catch (Exception e) {
            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public List<CourseModel> getAll() {
        return courseRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }

    @Override
    public CourseModel update(CourseItemDTO item) {
        try {
            Optional<CourseModel> op = courseRepository.findById(item.getCourseCode());

            if(op.isEmpty()) {
                throw new ValidationException("Course not exist!!!");
            }

            CourseModel courseModel = op.get();
            courseModel.setCourseName(item.getCourseName());
            courseModel.setDescription(item.getDescription());
            courseModel.setStatus(item.isStatus());
            courseModel.setPublic(item.isPublic());
            courseModel.setLevel(item.getLevel());
            courseModel.setSummary(item.getSummary());
            if(!StringUtil.isListEmpty(item.getThumbnail())) {
                String image = (String) item.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(item.getThumbnail().get(0).getType().contains("image/")) {
                    courseModel.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            courseModel = courseRepository.save(courseModel);
            return courseModel;
        } catch (Exception e) {
            log.error("update cour failed");
            log.error("{} | {}", e.getClass(), e.getMessage());
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public CourseModel getByCode(String code) {
        try {
            Optional<CourseModel> opCourse = courseRepository.findById(code);

            if(opCourse.isEmpty()) {
                throw new ValidationException("Course not exist!!!");
            }
            CourseModel courseModel = opCourse.get();
            courseModel.getSections().forEach(sec -> {
                List<LessonModel> lessons = sec.getLessons();
                lessons.sort((o1, o2) -> o1.getLesson_id() - o2.getLesson_id());

                sec.setLessons(lessons);
            });
            return courseModel;
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public Object getCoursesPublic(CourseRequest request, String token) {
        //request.setOwner(jwtTokenSetup.getUserIdFromToken(token));
        return courseRepository.getAll(request);
    }

    @Override
    @Transactional
    public void joinCourse(String courseCode, String token) {
        String userId = jwtTokenSetup.getUserIdFromToken(token);
        if(userId == null) {
            throw new ValidationException("user not found!!!");
        }

        CourseModel courseModel = courseRepository.getByCourseCode(courseCode);
        if(courseModel == null) {
            throw new ValidationException("course code not found!!");
        }

        StudentOfCourseModel soc = studentOfCourseRepository.getByCourseCodeAndUserId(courseCode, userId);
        if(soc == null) {
            soc = new StudentOfCourseModel();
            soc.setCourseCode(courseCode);
            soc.setUserId(userId);
            soc.setJoinAt(new Date());
            courseModel.setTotalStudent(courseModel.getTotalStudent() + 1);
        }

        studentOfCourseRepository.save(soc);
    }
}
