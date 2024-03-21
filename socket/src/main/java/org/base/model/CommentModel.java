package org.base.model;


import javax.persistence.*;

@Entity
@Table(name = "s_comment_model")
public class CommentModel extends AMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name ="COMMENT_ID")
    private Integer commentId;

    @Column(name = "PARENT_ID")
    private Integer parentId;

    @Transient
    private String fullname;

    @Transient
    private String avatar;


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getCommentId() {
        return commentId;
    }

    public void setCommentId(Integer commentId) {
        this.commentId = commentId;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
