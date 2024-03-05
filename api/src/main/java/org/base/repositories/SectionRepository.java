package org.base.repositories;

import org.base.model.course.SectionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<SectionModel, Integer> {

    SectionModel getBySectionName(String sectionName);

    @Modifying
    @Query(value = "delete from SectionModel where section_id = :sectionId")
    void deleteById(Integer sectionId);
}
