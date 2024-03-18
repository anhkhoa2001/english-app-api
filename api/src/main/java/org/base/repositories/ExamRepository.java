package org.base.repositories;

import org.base.dao.ExamDao;
import org.base.model.exam.ExamModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<ExamModel, String>, ExamDao {
}
