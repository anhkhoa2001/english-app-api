package org.base.dao.impl;

import org.base.dao.CourseDao;
import org.base.dto.course.CourseRequest;
import org.base.model.course.CourseModel;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
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

        sb.append(" SELECT * FROM U_COURSE WHERE 1 = 1 AND STATUS = true ");

        if(StringUtil.isObject(request.getIsPublic())) {
            sb.append(" AND is_public = :isPublic ");
            source.addValue("isPublic", request.getIsPublic());
        }

        if(request.getLevels() != null && !request.getLevels().isEmpty()) {
            sb.append(" AND level IN (:levels) ");
            source.addValue("levels", request.getLevels());
        }

        if(StringUtil.isObject(request.getRate())) {
            sb.append(" AND rate <= :rate ");
            source.addValue("rate", request.getRate());
        }

        sb.append(" ORDER BY CREATE_AT DESC LIMIT :pageSize OFFSET :page ");
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
            item.setLectures(Integer.parseInt(StringUtil.nvl(m.get("lectures"), "0")));
            item.setTotalStudent(Integer.parseInt(StringUtil.nvl(m.get("total_student"), "0")));
            item.setRate(Double.parseDouble(StringUtil.nvl(m.get("rate"), "0")));
            item.setStatus((boolean) m.get("status"));
            item.setPublic((boolean) m.get("is_public"));
            item.setCreateBy(StringUtil.nvl(m.get("create_by"), "admin"));
            item.setCreateAt(m.get("create_at") != null ? new Date(((Timestamp) m.get("create_at")).getTime()) : null);

            courses.add(item);
        }

        map.put("data", courses);

        sb = new StringBuilder();
        source = new MapSqlParameterSource();

        sb.append(" SELECT COUNT(*) FROM U_COURSE WHERE 1 = 1 AND STATUS = true ");

        if(StringUtil.isObject(request.getIsPublic())) {
            sb.append(" AND is_public = :isPublic ");
            source.addValue("isPublic", request.getIsPublic());
        }

        if(request.getLevels() != null && !request.getLevels().isEmpty()) {
            sb.append(" AND level IN (:levels) ");
            source.addValue("levels", request.getLevels());
        }

        if(StringUtil.isObject(request.getRate())) {
            sb.append(" AND rate <= :rate ");
            source.addValue("rate", request.getRate());
        }

        Integer count = namedParameterJdbcTemplate.queryForObject(sb.toString(), source, Integer.class);
        map.put("totalRecord", count);
        return map;
    }
}
