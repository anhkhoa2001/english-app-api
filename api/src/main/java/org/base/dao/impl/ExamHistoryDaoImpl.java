package org.base.dao.impl;

import org.base.dao.ExamHistoryDao;
import org.base.dto.UserDTO;
import org.base.dto.exam.ExamHistoryDTO;
import org.base.dto.exam.ExamListResponse;
import org.base.dto.exam.ExamRequest;
import org.base.model.exam.ExamHistoryModel;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class ExamHistoryDaoImpl implements ExamHistoryDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public ExamHistoryDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Object getByCondition(ExamHistoryDTO request) {
        List<ExamHistoryModel> exams = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * FROM u_exam_history e WHERE 1 = 1 ");

        if(request.getHistoryId() != null) {
            sb.append(" AND e.history_id = :historyId ");
            source.addValue("historyId", request.getHistoryId());
        }

        sb.append(" ORDER BY e.CREATE_TIME DESC  ");
        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            ExamHistoryModel item = new ExamHistoryModel();
            item.setHistoryId(m.get("HISTORY_ID") != null ? Integer.parseInt(m.get("HISTORY_ID").toString()) : 0);
            item.setExamCode(StringUtil.nvl(m.get("EXAM_CODE"), ""));
            item.setImplementer(StringUtil.nvl(m.get("IMPLEMENTER"), ""));
            item.setExecuteTime(m.get("EXECUTE_TIME") != null ? Integer.parseInt(m.get("EXECUTE_TIME").toString()) : 0);
            item.setResult(StringUtil.nvl(m.get("RESULT"), ""));
            item.setJson(StringUtil.nvl(m.get("JSON"), ""));
            item.setCreateTime(m.get("CREATE_TIME") != null ? new Date(((Timestamp) m.get("CREATE_TIME")).getTime()) : null);
            exams.add(item);
        }

        return exams;
    }
}
