package org.base.repositories;

import org.base.model.IndexModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;

@Repository
public interface IndexRepository extends JpaRepository<IndexModel, Long> {

    IndexModel getByCode(String code);

    @Modifying
    @Query(value = "UPDATE U_INDEX SET index = :index WHERE code = :code", nativeQuery = true)
    void update(Long index, String code);
}
