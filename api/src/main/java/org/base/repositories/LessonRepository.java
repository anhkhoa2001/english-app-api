package org.base.repositories;

import org.base.model.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, Integer> {

    @Query(value = "select * from u_lesson where lesson_name = :lessonName and section_ref_id = :sectionId", nativeQuery = true)
    LessonModel getByLessionNameAndSectionR(String lessonName, Integer sectionId);
}
