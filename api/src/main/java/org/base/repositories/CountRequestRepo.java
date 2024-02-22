package org.base.repositories;

import org.base.model.CountRequestModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountRequestRepo extends JpaRepository<CountRequestModel, Integer> {
}
