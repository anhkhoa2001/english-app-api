package org.base.model.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.base.model.exam.ExamModel;
import org.base.model.exam.ExamPartModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "u_lesson")
@Data
public class LessonModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer lesson_id;

    @Column(name = "LESSON_NAME", nullable = false)
    private String lessionName;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Column(name = "URL_VIDEO")
    private String url_video;

    @Column(name = "TYPE")
    private String type;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "exam_part_id", referencedColumnName = "ID")
    private ExamPartModel examModel;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @ManyToOne
    @JoinColumn(name="section_ref_id", nullable=false)
    @JsonIgnore
    private SectionModel sectionModel;

    @Transient
    private Integer section_id;

    public Integer getSection_id() {
        return sectionModel != null ? sectionModel.getSection_id() : null;
    }
}
