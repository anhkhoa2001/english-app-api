package org.base.repositories;

import org.base.dao.BlogDao;
import org.base.model.blog.BlogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlogRepository extends JpaRepository<BlogModel, Integer>, BlogDao {
}
