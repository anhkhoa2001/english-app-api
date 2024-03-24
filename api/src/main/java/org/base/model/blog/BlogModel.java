package org.base.model.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.dto.UserDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "u_blog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "BLOG_ID")
    private Integer blogId;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "SKILL")
    private String skill;

    @Column(name = "ENGLISH_BASIC")
    private String englishBasic;

    @Column(name = "ENGLISH_FOR")
    private String englishFor;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "STATUS")
    private boolean status;

    @Transient
    private UserDTO author;
}
