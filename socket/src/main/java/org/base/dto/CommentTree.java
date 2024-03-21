package org.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.model.CommentModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentTree {

    private boolean hasChild;
    private Integer commentId;
    private List<CommentTree> childrens;
    private String name;
    private String avatar;
    private String content;
    private Date sendTime;
    private Integer parentId;
    public CommentTree(CommentModel commentModel) {
        this.sendTime = commentModel.getSendTime();
        this.commentId = commentModel.getCommentId();
        this.parentId = commentModel.getParentId();
        this.content = commentModel.getContent();
        this.childrens = new ArrayList<>();
        this.avatar = commentModel.getAvatar();
        this.name = commentModel.getFullname();
    }
}
