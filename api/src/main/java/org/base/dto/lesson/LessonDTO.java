package org.base.dto.lesson;

import lombok.Data;
import org.base.dto.FileDTO;

import java.util.List;

@Data
public class LessonDTO {

    private List<FileDTO> thumbnail;
    private List<FileDTO> video;

}
