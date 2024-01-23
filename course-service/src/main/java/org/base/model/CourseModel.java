package org.base.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "C_COURSE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseModel {

    @Id
    private String code;

    @Column(name = "COURSE_NAME")
    private String courseName;

    @Column(name = "CREATE_AT")
    private Date createAt;
}
