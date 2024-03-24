package org.base.services.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.config.JwtTokenSetup;
import org.base.dto.blog.BlogDTO;
import org.base.dto.blog.BlogRequest;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.blog.BlogModel;
import org.base.repositories.BlogRepository;
import org.base.services.BlogService;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final JwtTokenSetup jwtTokenSetup;

    @Autowired
    public BlogServiceImpl(BlogRepository blogRepository,
                           JwtTokenSetup jwtTokenSetup) {
        this.blogRepository = blogRepository;
        this.jwtTokenSetup = jwtTokenSetup;
    }

    @Override
    public BlogModel create(BlogDTO request, String token) {
        try {
            String userId = jwtTokenSetup.getUserIdFromToken(token);

            BlogModel blogModel = new BlogModel();
            blogModel.setCreateAt(new Date());
            blogModel.setCreateBy(userId);
            blogModel.setContent(request.getContent());
            blogModel.setSkill(String.join(",", request.getSkill()));
            blogModel.setEnglishBasic(String.join(",", request.getEnglishBasic()));
            blogModel.setEnglishFor(String.join(",", request.getEnglishFor()));
            blogModel.setStatus(request.isStatus());
            blogModel.setSummary(request.getSummary());
            blogModel.setTitle(request.getTitle());
            if(!StringUtil.isListEmpty(request.getThumbnail())) {
                String image = (String) request.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(request.getThumbnail().get(0).getType().contains("image/")) {
                    blogModel.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }
            blogModel = blogRepository.save(blogModel);
            return blogModel;
        } catch (Exception e) {
            log.error("create blog failed!!");
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public BlogModel update(BlogDTO request) {
        try {
            Optional<BlogModel> opBlog = blogRepository.findById(request.getBlogId());

            if(opBlog.isEmpty()) {
                throw new ValidationException("blog is not exist!!");
            }
            BlogModel blogModel = opBlog.get();
            blogModel.setContent(request.getContent());
            blogModel.setSkill(String.join(",", request.getSkill()));
            blogModel.setEnglishBasic(String.join(",", request.getEnglishBasic()));
            blogModel.setEnglishFor(String.join(",", request.getEnglishFor()));
            blogModel.setStatus(request.isStatus());
            blogModel.setSummary(request.getSummary());
            blogModel.setTitle(request.getTitle());
            if(!StringUtil.isListEmpty(request.getThumbnail())) {
                String image = (String) request.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(request.getThumbnail().get(0).getType().contains("image/")) {
                    blogModel.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            blogModel = blogRepository.save(blogModel);
            return blogModel;
        } catch (Exception e) {
            log.error("update blog failed!!");
            e.printStackTrace();
            throw new SystemException(e.getMessage());
        }
    }

    @Override
    public List<BlogModel> getAll() {
        return blogRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }

    @Override
    public Object getByCondition(BlogRequest request, String token) {
        return blogRepository.getByCondition(request, token);
    }
}
