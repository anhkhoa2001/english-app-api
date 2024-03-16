package org.base.repositories;

import org.base.model.course.StudentOfCourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentOfCourseRepository extends JpaRepository<StudentOfCourseModel, Integer> {

    StudentOfCourseModel getByCourseCodeAndUserId(String courseCode, String userId);
}
