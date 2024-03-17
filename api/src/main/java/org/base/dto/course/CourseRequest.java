package org.base.dto.course;

import lombok.Data;

import java.util.Set;

@Data
public class CourseRequest {

    private long page;
    private long pageSize;
    private Boolean isPublic;
    private String owner;
    private Set<String> levels;
    private Integer rate;
}
