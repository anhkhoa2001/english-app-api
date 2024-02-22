package org.base.dao;

import org.base.dto.course.CourseRequest;
import org.base.model.CourseModel;

import java.util.List;
import java.util.Map;

public interface CourseDao {

    Map<String, Object> getAll(CourseRequest request);
}
