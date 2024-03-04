package org.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "U_SECTION")
@Getter
@Setter
public class SectionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer section_id;

    @ManyToOne
    @JoinColumn(name="course_code", nullable=false)
    @JsonIgnore
    private CourseModel courseModel;

    @Column(name = "SECTION_NAME", nullable = false)
    private String sectionName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private boolean status;

    @Transient
    private String courseCode;

    @OneToMany(mappedBy="sectionModel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LessonModel> lessons;
}
