package org.base.dto.lesson;

import lombok.Data;
import org.base.dto.FileDTO;

import java.util.List;

@Data
public class LessonDTO {

    private List<FileDTO> thumbnail;
    private List<FileDTO> video;
    private int section_id;
    private String sectionName;
    private String lessonName;
    private boolean status;
    private String des;
}
