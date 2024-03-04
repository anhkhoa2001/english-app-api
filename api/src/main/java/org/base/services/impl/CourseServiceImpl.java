package org.base.services.impl;

import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.exception.AppException;
import org.base.exception.ValidationException;
import org.base.model.CourseModel;
import org.base.repositories.CourseRepository;
import org.base.services.CourseService;
import org.base.utils.StringUtil;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public CourseModel create(CourseItemDTO item) {
        try {
            Optional<CourseModel> op = courseRepository.findById(item.getCourseCode());

            if(op.isPresent()) {
                throw new ValidationException("Course already exist!!!");
            }

            CourseModel courseModel = new CourseModel();
            courseModel.setCode(item.getCourseCode());
            courseModel.setCourseName(item.getCourseName());
            courseModel.setDescription(item.getDescription());
            courseModel.setCreateAt(new Date());
            //tam thoi chua co security
            //de null
            courseModel.setCreateBy(null);
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
            throw new AppException(e.getMessage());
        }
    }

    @Override
    public List<CourseModel> getAll(CourseRequest request) {
        return courseRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }

    @Override
    public CourseModel update(CourseItemDTO item) {
        Optional<CourseModel> op = courseRepository.findById(item.getCourseCode());

        if(op.isEmpty()) {
            throw new ValidationException("Course not exist!!!");
        }

        CourseModel courseModel = op.get();
        courseModel.setCourseName(item.getCourseName());
        courseModel.setDescription(item.getDescription());
        //tam thoi chua co security
        //de null
        courseModel.setCreateBy(null);
        courseModel.setStatus(item.isStatus());
        courseModel.setPublic(item.isPublic());
        courseModel.setLevel(item.getLevel());
        courseModel.setSummary(item.getSummary());
        //courseModel.setThumbnail(item.getThumbnail());

        courseModel = courseRepository.save(courseModel);
        return courseModel;
    }

    @Override
    public CourseModel getByCode(String code) {
        try {
            CourseModel courseModel = courseRepository.findById(code).get();
            return courseModel;
        } catch (Exception e) {
        }
        return null;
    }
}
