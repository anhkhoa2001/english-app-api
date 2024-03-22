package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.base.model.course.LessonModel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "u_exam")
@Getter
@Setter
public class ExamModel {

    @Id
    @Column(name = "EXAM_CODE")
    private String examCode;

    @Column(name = "EXAM_NAME")
    private String examName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "SKILL")
    private String skill;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "ATTENDENCES", nullable = false)
    private int attendences;

    @Column(name = "COUNTDOWN")
    private int countdown;

    @Column(name = "TOTAL_QUESTION")
    private int totalQuestion;

    @Column(name = "TARGET")
    private int target;

    @OneToMany(mappedBy = "examModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExamPartModel> parts;
}
