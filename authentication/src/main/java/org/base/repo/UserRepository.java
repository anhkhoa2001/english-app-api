package org.base.repo;

import org.base.entity.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {

    @Query(value = "SELECT * FROM U_USER WHERE USERNAME = :username AND TYPE = :type", nativeQuery = true)
    UserModel getByUsernameAndType(String username, String type);

    @Query(value = "SELECT * FROM U_USER WHERE USERNAME = :username", nativeQuery = true)
    UserModel getByUsername(String username);

}
