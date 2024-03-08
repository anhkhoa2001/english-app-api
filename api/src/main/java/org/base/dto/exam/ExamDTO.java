package org.base.dto.exam;

import lombok.Data;
import org.base.dto.FileDTO;

import java.util.List;

@Data
public class ExamDTO {

    private String examCode;

    private String examName;

    private String description;

    private String summary;

    private String skill;

    private boolean status;

    private String type;

    private List<FileDTO> thumbnail;
}
