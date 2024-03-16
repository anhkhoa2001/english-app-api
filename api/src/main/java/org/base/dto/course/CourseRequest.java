package org.base.dto.course;

import lombok.Data;

@Data
public class CourseRequest {

    private long page;
    private long pageSize;
    private Boolean isPublic;
    private String owner;
}
