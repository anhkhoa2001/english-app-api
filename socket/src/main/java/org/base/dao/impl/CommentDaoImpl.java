package org.base.dao.impl;

import org.base.utils.StringUtil;
import org.base.dao.CommentDao;
import org.base.model.CommentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class CommentDaoImpl implements CommentDao {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public List<CommentModel> getAllByRefIdAndEntityRef(String refId, String entityRef) {
        List<CommentModel> comments = new ArrayList<>();
        MapSqlParameterSource source = new MapSqlParameterSource();
        StringBuilder sb = new StringBuilder();

        sb.append("SELECT s.*, u.fullname, u.avatar FROM s_comment_model s\n" +
                "left join u_user u on s.sender = u.user_id\n" +
                "order by s.parent_id asc, s.send_time asc");


        List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sb.toString(), source);

        for (Map m : results) {
            CommentModel item = new CommentModel();
            item.setCommentId(m.get("comment_id") != null ? Integer.parseInt(m.get("comment_id").toString()) : 0);
            item.setParentId(m.get("parent_id") != null ? Integer.parseInt(m.get("parent_id").toString()) : 0);
            item.setFullname(StringUtil.nvl(m.get("fullname"), ""));
            item.setAvatar(StringUtil.nvl(m.get("avatar"), ""));
            item.setSender(StringUtil.nvl(m.get("sender"), ""));
            item.setEntityRef(StringUtil.nvl(m.get("entityRef"), ""));
            item.setRefId(StringUtil.nvl(m.get("refId"), ""));
            item.setContent(StringUtil.nvl(m.get("content"), ""));
            item.setSendTime(m.get("send_time") != null ? new Date(((Timestamp) m.get("send_time")).getTime()) : null);

            comments.add(item);
        }

        return comments;
    }
}
