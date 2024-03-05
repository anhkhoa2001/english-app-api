package org.base.services;

import org.base.dto.lesson.LessonDTO;
import org.base.model.course.LessonModel;

public interface LessonService {

    LessonModel create(LessonDTO dto);

    LessonModel getByLessonId(Integer lessonId);

    void deleteByLessonId(Integer lessonId);

    LessonModel update(LessonDTO dto);
}
