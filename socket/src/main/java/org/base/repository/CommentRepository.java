package org.base.repository;

import org.base.dao.CommentDao;
import org.base.model.CommentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<CommentModel, Integer>, CommentDao {

    /*@Query(value = "SELECT s.*, u.fullname, u.avatar FROM s_comment_model s\n" +
                "left join u_user u on s.sender = u.user_id\n" +
                "order by s.parent_id asc, s.send_time asc", nativeQuery = true)
    List<CommentModel> getAllByRefIdAndEntityRef(String refId, String entityRef);*/
}
