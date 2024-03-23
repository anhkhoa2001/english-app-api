package org.base.dto.exam;

import lombok.Data;
import org.base.dto.UserDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ExamListResponse {

    private String examCode;

    private String examName;

    private String description;

    private String summary;

    private String skill;

    private String type;

    private boolean status;

    private String thumbnail;

    private String createBy;

    private Date createAt;

    private int attendences;

    private UserDTO author;

    private List<ExamPartDTO> parts;

    public ExamListResponse() {
        parts = new ArrayList<>();
    }
}
