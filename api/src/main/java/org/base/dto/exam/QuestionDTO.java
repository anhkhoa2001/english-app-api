package org.base.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDTO {
    private Integer questionId;
    private String examCode;
    private int part;
    private int partPoint;
    private String content;
    private String type;
    private List<QuestionItemDTO> questions;
}
