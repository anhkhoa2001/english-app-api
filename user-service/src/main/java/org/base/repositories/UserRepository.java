package org.base.repositories;

import org.base.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    @Query(value = "SELECT * FROM E_USER WHERE USERNAME = :username AND TYPE = :type", nativeQuery = true)
    UserModel getByUsernameAndType(String username, String type);
}
