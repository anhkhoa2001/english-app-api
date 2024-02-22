package org.base.dto.course;

import lombok.Data;
import org.base.dto.FileDTO;

import java.util.List;

@Data
public class CourseItemDTO {
    private String courseCode;
    private String courseName;
    private String description;
    private String level;
    private boolean isPublic;
    private boolean status;
    private List<FileDTO> thumbnail;
    private String summary;
}
