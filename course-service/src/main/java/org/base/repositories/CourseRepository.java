package org.base.repositories;

import org.base.model.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, String> {

    List<CourseModel> getByCourseName(String coursename);

}
