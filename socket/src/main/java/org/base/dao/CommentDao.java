package org.base.dao;

import org.base.model.CommentModel;

import java.util.List;

public interface CommentDao {

    List<CommentModel> getAllByRefIdAndEntityRef(String refId, String entityRef);
}
