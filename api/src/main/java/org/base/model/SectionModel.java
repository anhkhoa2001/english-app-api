package org.base.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "U_SECTION")
@Data
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

    @OneToMany(mappedBy="sectionModel", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LessonModel> lessons;
}
