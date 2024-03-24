package org.base.dao;

import org.base.dto.blog.BlogRequest;

public interface BlogDao {

    Object getByCondition(BlogRequest request, String userId);
}
