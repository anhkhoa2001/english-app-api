package org.base.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "U_LESSON")
@Data
public class LessonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "LESSON_NAME", nullable = false)
    private String lessionName;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "URL_VIDEO")
    private String url_video;

    @Column(name = "DESCRIPTION", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="section_id", nullable=false)
    private SectionModel sectionModel;
}
