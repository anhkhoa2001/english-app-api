package org.base.repositories;

import org.base.model.course.LessonModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonModel, Integer> {

    @Query(value = "select * from u_lesson where lesson_name = :lessonName and section_ref_id = :sectionId", nativeQuery = true)
    LessonModel getByLessionNameAndSectionR(String lessonName, Integer sectionId);

    @Modifying
    @Query(value = "delete from LessonModel where lesson_id in (:lessonId)")
    void deleteByListId(List<Integer> lessonId);
}
