package org.base.repositories;

import org.base.dao.CourseDao;
import org.base.model.course.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, String>, CourseDao {
}
