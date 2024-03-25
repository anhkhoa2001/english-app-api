package org.base.dao.impl;

import org.base.dao.DocumentDao;
import org.base.dto.UserDTO;
import org.base.dto.blog.BlogRequest;
import org.base.dto.docs.DocumentRequest;
import org.base.model.blog.BlogModel;
import org.base.model.docs.DocumentModel;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.*;

@Repository
public class DocumentDaoImpl implements DocumentDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Object getAllPublic(DocumentRequest request) {
        Map<String, Object> map = new HashMap<>();
        List<DocumentModel> documents = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append(" SELECT * FROM u_document d  left join u_user u on d.create_by = u.user_id " +
                  " WHERE 1 = 1 AND d.STATUS = true ");

        if(request.getSkill() != null && !request.getSkill().isEmpty()) {
            sb.append(" AND lower(d.skill) similar to concat('%(',:skill,'%)') ");
            source.addValue("skill", String.join("|", request.getSkill()));
        }

        if(request.getTopic() != null && !request.getTopic().isEmpty()) {
            sb.append(" AND lower(d.topic) similar to concat('%(',:topic,'%)') ");
            source.addValue("topic", String.join("|", request.getTopic()));
        }

        sb.append(" ORDER BY d.CREATE_AT DESC LIMIT :pageSize OFFSET :page ");
        source.addValue("pageSize", request.getPageSize());
        source.addValue("page", (request.getPage() - 1)*request.getPageSize());

        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            DocumentModel item = new DocumentModel();
            item.setDocumentId(m.get("document_id") != null ? Integer.parseInt(m.get("document_id").toString()) : 0);
            item.setDocumentName(StringUtil.nvl(m.get("document_name"), ""));
            item.setLink(StringUtil.nvl(m.get("link"), ""));
            item.setThumbnail(StringUtil.nvl(m.get("thumbnail"), ""));
            item.setSummary(StringUtil.nvl(m.get("summary"), ""));
            item.setType(StringUtil.nvl(m.get("type"), ""));
            item.setTopic(StringUtil.nvl(m.get("topic"), ""));
            item.setSkill(StringUtil.nvl(m.get("skill"), ""));
            item.setStatus((boolean) m.get("status"));
            item.setCreateBy(StringUtil.nvl(m.get("create_by"), "admin"));
            item.setCreateAt(m.get("create_at") != null ? new Date(((Timestamp) m.get("create_at")).getTime()) : null);
            item.setAuthor(UserDTO.builder()
                    .avatar(StringUtil.nvl(m.get("AVATAR"), ""))
                    .fullname(StringUtil.nvl(m.get("FULLNAME"), ""))
                    .build());
            documents.add(item);
        }

        map.put("data", documents);

        sb = new StringBuilder();
        source = new MapSqlParameterSource();

        sb.append(" SELECT COUNT(*) FROM u_document d  left join u_user u on d.create_by = u.user_id " +
                " WHERE 1 = 1 AND d.STATUS = true ");

        if(request.getSkill() != null && !request.getSkill().isEmpty()) {
            sb.append(" AND lower(d.skill) similar to concat('%(',:skill,'%)') ");
            source.addValue("skill", String.join("|", request.getSkill()));
        }

        if(request.getTopic() != null && !request.getTopic().isEmpty()) {
            sb.append(" AND lower(d.topic) similar to concat('%(',:topic,'%)') ");
            source.addValue("topic", String.join("|", request.getTopic()));
        }

        Integer count = namedParameterJdbcTemplate.queryForObject(sb.toString(), source, Integer.class);
        map.put("totalRecord", count);
        return map;
    }
}
