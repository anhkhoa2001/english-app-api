package org.base.repositories;

import org.base.dao.DocumentDao;
import org.base.model.docs.DocumentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentModel, Integer>, DocumentDao {
}
