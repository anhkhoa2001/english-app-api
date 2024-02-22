package org.base.services;

import org.base.dto.course.CourseItemDTO;
import org.base.dto.course.CourseRequest;
import org.base.model.CourseModel;

import java.util.List;
import java.util.Map;

public interface CourseService {

    CourseModel create(CourseItemDTO item);

    Map<String, Object> getAll(CourseRequest request);

    CourseModel update(CourseItemDTO item);

    CourseModel getByCode(String code);
}
