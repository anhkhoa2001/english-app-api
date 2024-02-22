package org.base.dao.impl;

import org.base.dao.CourseDao;
import org.base.dto.course.CourseRequest;
import org.base.model.CourseModel;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Component
public class CourseDaoImpl implements CourseDao {

    @Autowired
    private  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Map<String, Object> getAll(CourseRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<CourseModel> courses = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * FROM U_COURSE ");


        sb.append(" LIMIT :pageSize OFFSET :page ");
        source.addValue("pageSize", request.getPageSize());
        source.addValue("page", (request.getPage() - 1)*request.getPageSize());

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            CourseModel item = new CourseModel();
            item.setCode(StringUtil.nvl(m.get("code"), ""));
            item.setCourseName(StringUtil.nvl(m.get("course_name"), ""));
            item.setDescription(StringUtil.nvl(m.get("description"), ""));
            item.setLevel(StringUtil.nvl(m.get("level"), ""));
            item.setSummary(StringUtil.nvl(m.get("summary"), ""));
            item.setThumbnail(StringUtil.nvl(m.get("thumbnail"), ""));
            item.setStatus((boolean) m.get("status"));
            item.setPublic((boolean) m.get("is_public"));
            item.setCreateBy(StringUtil.nvl(m.get("create_by"), "admin"));
            item.setCreateAt(m.get("create_at") != null ? new Date(((Timestamp) m.get("create_at")).getTime()) : null);

            courses.add(item);
        }

        map.put("data", courses);

        sb = new StringBuilder();
        source = new MapSqlParameterSource();

        sb.append(" SELECT COUNT(*) FROM U_COURSE ");

        Integer count = namedParameterJdbcTemplate.queryForObject(sb.toString(), source, Integer.class);
        map.put("totalRecord", count);
        return map;
    }
}
