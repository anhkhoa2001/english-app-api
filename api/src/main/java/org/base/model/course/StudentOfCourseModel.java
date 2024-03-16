package org.base.model.course;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "u_student_of_course")
@Data
public class StudentOfCourseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "row_id")
    private Integer rowId;

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "join_at")
    private Date joinAt;
}
