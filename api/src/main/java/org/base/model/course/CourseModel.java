package org.base.model.course;

import lombok.*;
import org.base.model.UserModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "U_COURSE")
@Getter
@Setter
public class CourseModel implements Serializable {

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

    @Column(name = "LECTURES")
    private int lectures;

    @Column(name = "total_student")
    private int total_student;

    @OneToMany(mappedBy = "courseModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SectionModel> sections;
}

