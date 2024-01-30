package org.base.repositories;

import org.base.model.IndexModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<IndexModel, Long> {

    IndexModel getByCode(String code);
}
