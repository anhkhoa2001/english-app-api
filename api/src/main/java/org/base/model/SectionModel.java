package org.base.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "U_SECTION")
@Data
public class SectionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="course_code", nullable=false)
    private CourseModel courseModel;

    @Column(name = "SECTION_NAME", nullable = false)
    private String sectionName;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "STATUS")
    private boolean status;

    @OneToMany(mappedBy="lesson", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<LessonModel> lessons;
}
