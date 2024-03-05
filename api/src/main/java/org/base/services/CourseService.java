package org.base.services;

import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.model.course.CourseModel;

import java.util.List;

public interface CourseService {

    CourseModel create(CourseItemDTO item);

    List<CourseModel> getAll(CourseRequest request);

    CourseModel update(CourseItemDTO item);

    CourseModel getByCode(String code);
}
