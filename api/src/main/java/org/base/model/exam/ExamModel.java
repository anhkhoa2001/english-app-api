package org.base.model.exam;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
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

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @OneToMany(mappedBy = "examModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExamPartModel> parts;
}
