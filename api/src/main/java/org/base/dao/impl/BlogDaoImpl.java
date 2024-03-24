package org.base.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.base.dao.BlogDao;
import org.base.dto.UserDTO;
import org.base.dto.blog.BlogRequest;
import org.base.model.blog.BlogModel;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Repository
public class BlogDaoImpl implements BlogDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    @Override
    public Object getByCondition(BlogRequest request, String userId) {
        Map<String, Object> map = new HashMap<>();
        List<BlogModel> blogs = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * FROM u_blog b \n" +
                " left join u_user u on b.create_by = u.user_id WHERE 1 = 1 AND b.STATUS = true ");

        if(request.getSkill() != null && !request.getSkill().isEmpty()) {
            sb.append(" AND lower(b.skill) similar to concat('%(',:skill,'%)') ");
            source.addValue("skill", String.join("|", request.getSkill()));
        }

        if(request.getEnglishBasic() != null && !request.getEnglishBasic().isEmpty()) {
            sb.append(" AND lower(b.english_basic) similar to concat('%(',:english_basic,'%)') ");
            source.addValue("english_basic", String.join("|", request.getEnglishBasic()));
        }

        if(request.getEnglishFor() != null && !request.getEnglishFor().isEmpty()) {
            sb.append(" AND lower(b.english_for) similar to concat('%(',:english_for,'%)') ");
            source.addValue("english_for", String.join("|", request.getEnglishFor()));
        }

        sb.append(" ORDER BY b.CREATE_AT DESC LIMIT :pageSize OFFSET :page ");
        source.addValue("pageSize", request.getPageSize());
        source.addValue("page", (request.getPage() - 1)*request.getPageSize());

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            BlogModel item = new BlogModel();
            item.setBlogId(m.get("blog_id") != null ? Integer.parseInt(m.get("blog_id").toString()) : 0);
            item.setTitle(StringUtil.nvl(m.get("title"), ""));
            item.setContent(StringUtil.nvl(m.get("content"), ""));
            item.setThumbnail(StringUtil.nvl(m.get("thumbnail"), ""));
            item.setSummary(StringUtil.nvl(m.get("summary"), ""));
            item.setEnglishFor(StringUtil.nvl(m.get("english_for"), ""));
            item.setEnglishBasic(StringUtil.nvl(m.get("english_basic"), ""));
            item.setSkill(StringUtil.nvl(m.get("skill"), ""));
            item.setStatus((boolean) m.get("status"));
            item.setCreateBy(StringUtil.nvl(m.get("create_by"), "admin"));
            item.setCreateAt(m.get("create_at") != null ? new Date(((Timestamp) m.get("create_at")).getTime()) : null);
            item.setAuthor(UserDTO.builder()
                    .avatar(StringUtil.nvl(m.get("AVATAR"), ""))
                    .fullname(StringUtil.nvl(m.get("FULLNAME"), ""))
                    .build());
            blogs.add(item);
        }

        map.put("data", blogs);

        sb = new StringBuilder();
        source = new MapSqlParameterSource();

        sb.append(" SELECT COUNT(*) FROM u_blog b \n" +
                " left join u_user u on b.create_by = u.user_id WHERE 1 = 1 AND b.STATUS = true ");

        if(request.getSkill() != null && !request.getSkill().isEmpty()) {
            sb.append(" AND lower(b.skill) similar to concat('%(',:skill,'%)') ");
            source.addValue("skill", String.join("|", request.getSkill()));
        }

        if(request.getEnglishBasic() != null && !request.getEnglishBasic().isEmpty()) {
            sb.append(" AND lower(b.english_basic) similar to concat('%(',:english_basic,'%)') ");
            source.addValue("english_basic", String.join("|", request.getEnglishBasic()));
        }

        if(request.getEnglishFor() != null && !request.getEnglishFor().isEmpty()) {
            sb.append(" AND lower(b.english_for) similar to concat('%(',:english_for,'%)') ");
            source.addValue("english_for", String.join("|", request.getEnglishFor()));
        }

        Integer count = namedParameterJdbcTemplate.queryForObject(sb.toString(), source, Integer.class);
        map.put("totalRecord", count);
        return map;
    }
}
