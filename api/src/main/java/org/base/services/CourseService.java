package org.base.services;

import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.model.course.CourseModel;

import java.util.List;

public interface CourseService {

    CourseModel create(CourseItemDTO item);

    List<CourseModel> getAll();

    CourseModel update(CourseItemDTO item);

    CourseModel getByCode(String code);

    Object getCoursesPublic(CourseRequest request, String token);

    void joinCourse(String courseCode, String token);
}
