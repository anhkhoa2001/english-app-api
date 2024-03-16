package org.base.repositories;

import org.base.dao.CourseDao;
import org.base.model.course.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface CourseRepository extends JpaRepository<CourseModel, String>, CourseDao {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query(value = "select c from CourseModel c where code = :courseCode ")
    CourseModel getByCourseCode(String courseCode);

}
