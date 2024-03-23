package org.base.dao.impl;

import org.base.dao.ExamDao;
import org.base.dto.UserDTO;
import org.base.dto.course.CourseRequest;
import org.base.dto.exam.ExamListResponse;
import org.base.dto.exam.ExamPartDTO;
import org.base.dto.exam.ExamRequest;
import org.base.model.course.CourseModel;
import org.base.model.exam.ExamModel;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class ExamDaoImpl implements ExamDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Override
    public Object getAllExam(ExamRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<ExamListResponse> exams = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * FROM U_EXAM e LEFT JOIN u_user u on e.create_by = u.user_id WHERE 1 = 1 AND e.STATUS = true ");

        if(request.getTypes() != null && !request.getTypes().isEmpty()) {
            sb.append(" AND e.type IN (:type) ");
            source.addValue("type", request.getTypes());
        } else {
            sb.append(" AND e.type != :type ");
            source.addValue("type", "Minitest");
        }

        if(request.getSkills() != null && !request.getSkills().isEmpty()) {
            sb.append(" AND e.skill IN (:skill) ");
            source.addValue("skill", request.getSkills());
        }

        sb.append(" ORDER BY e.CREATE_AT DESC LIMIT :pageSize OFFSET :page ");
        source.addValue("pageSize", request.getPageSize());
        source.addValue("page", (request.getPage() - 1)*request.getPageSize());

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            ExamListResponse item = new ExamListResponse();
            item.setExamCode(StringUtil.nvl(m.get("EXAM_CODE"), ""));
            item.setExamName(StringUtil.nvl(m.get("EXAM_NAME"), ""));
            item.setDescription(StringUtil.nvl(m.get("DESCRIPTION"), ""));
            item.setSummary(StringUtil.nvl(m.get("summary"), ""));
            item.setThumbnail(StringUtil.nvl(m.get("thumbnail"), ""));
            item.setSkill(StringUtil.nvl(m.get("SKILL"), ""));
            item.setType(StringUtil.nvl(m.get("TYPE"), ""));
            item.setAttendences(Integer.parseInt(StringUtil.nvl(m.get("ATTENDENCES"), "0")));
            item.setStatus((boolean) m.get("status"));
            item.setCreateBy(StringUtil.nvl(m.get("create_by"), "admin"));
            item.setCreateAt(m.get("create_at") != null ? new Date(((Timestamp) m.get("create_at")).getTime()) : null);

            item.setAuthor(UserDTO.builder()
                            .avatar(StringUtil.nvl(m.get("AVATAR"), ""))
                            .fullname(StringUtil.nvl(m.get("FULLNAME"), ""))
                            .build());

            exams.add(item);
        }

        map.put("data", exams);

        sb = new StringBuilder();
        source = new MapSqlParameterSource();

        sb.append(" SELECT COUNT(*) FROM U_EXAM e WHERE 1 = 1 AND e.STATUS = true ");

        if(request.getTypes() != null && !request.getTypes().isEmpty()) {
            sb.append(" AND e.type IN (:type) ");
            source.addValue("type", request.getTypes());
        } else {
            sb.append(" AND e.type != :type ");
            source.addValue("type", "Minitest");
        }

        if(request.getSkills() != null && !request.getSkills().isEmpty()) {
            sb.append(" AND e.skill IN (:skill) ");
            source.addValue("skill", request.getSkills());
        }

        Integer count = namedParameterJdbcTemplate.queryForObject(sb.toString(), source, Integer.class);
        map.put("totalRecord", count);
        return map;
    }

    @Override
    public Object getExamByCondition(ExamRequest request) {
        List<ExamListResponse> exams = new ArrayList<>();
        Map<String, ExamListResponse> map = new HashMap<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * FROM U_EXAM e\n" +
                    "LEFT JOIN u_user u on e.create_by = u.user_id\n" +
                    "LEFT JOIN u_exam_part p on e.exam_code = p.exam_code\n" +
                    "WHERE 1 = 1 AND e.STATUS = true ");

        if(request.getTypes() != null && !request.getTypes().isEmpty()) {
            sb.append(" AND e.type IN (:type) ");
            source.addValue("type", request.getTypes());
        } else {
            sb.append(" AND e.type != :type ");
            source.addValue("type", "Minitest");
        }

        if(request.getSkills() != null && !request.getSkills().isEmpty()) {
            sb.append(" AND e.skill IN (:skill) ");
            source.addValue("skill", request.getSkills());
        }

        sb.append(" ORDER BY e.CREATE_AT DESC ");

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            String examCode = StringUtil.nvl(m.get("EXAM_CODE"), "");
            ExamListResponse item = map.getOrDefault(examCode, new ExamListResponse());
            item.setExamCode(StringUtil.nvl(m.get("EXAM_CODE"), ""));
            item.setExamName(StringUtil.nvl(m.get("EXAM_NAME"), ""));
            item.setDescription(StringUtil.nvl(m.get("DESCRIPTION"), ""));
            item.setSummary(StringUtil.nvl(m.get("summary"), ""));
            item.setThumbnail(StringUtil.nvl(m.get("thumbnail"), ""));
            item.setSkill(StringUtil.nvl(m.get("SKILL"), ""));
            item.setType(StringUtil.nvl(m.get("TYPE"), ""));
            item.setAttendences(Integer.parseInt(StringUtil.nvl(m.get("ATTENDENCES"), "0")));
            item.setStatus((boolean) m.get("status"));
            item.setCreateBy(StringUtil.nvl(m.get("create_by"), "admin"));
            item.setCreateAt(m.get("create_at") != null ? new Date(((Timestamp) m.get("create_at")).getTime()) : null);

            item.setAuthor(UserDTO.builder()
                    .avatar(StringUtil.nvl(m.get("AVATAR"), ""))
                    .fullname(StringUtil.nvl(m.get("FULLNAME"), ""))
                    .build());

            if(m.get("part_id") != null) {
                Integer partId = Integer.parseInt(m.get("part_id").toString());
                Integer id = Integer.parseInt(m.get("id").toString());
                item.getParts().add(new ExamPartDTO(partId, id));
            }
            map.put(examCode, item);
        }

        return map.values();
    }
}
