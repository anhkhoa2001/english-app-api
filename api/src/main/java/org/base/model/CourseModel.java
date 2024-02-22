package org.base.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Data
@Entity
@Table(name = "U_COURSE")
public class CourseModel {

    @Id
    private String code;
    @Column(name = "COURSE_NAME")
    private String courseName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "LEVEL")
    private String level;

    @Column(name = "IS_PUBLIC")
    private boolean isPublic;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "TOTAL_SUB")
    private int totalSub;

    @Column(name = "RATE")
    private double rate;

    @OneToMany(mappedBy="section", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<SectionModel> sections;

}
