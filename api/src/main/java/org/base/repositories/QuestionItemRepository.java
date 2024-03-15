package org.base.repositories;

import org.base.model.exam.QuestionItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionItemRepository extends JpaRepository<QuestionItemModel, Integer> {
}
