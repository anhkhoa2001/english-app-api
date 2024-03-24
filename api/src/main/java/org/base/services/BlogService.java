package org.base.services;

import org.base.dto.blog.BlogDTO;
import org.base.dto.blog.BlogRequest;
import org.base.model.blog.BlogModel;

import java.util.List;

public interface BlogService {

    BlogModel create(BlogDTO request, String token);


    BlogModel update(BlogDTO request);

    List<BlogModel> getAll();

    Object getByCondition(BlogRequest request, String token);
}
