package org.base.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionItemDTO {

    private int index;
    private String content;
    private String type;
    private String solution;
    private List<AnswerDTO> answer;
    private String hint;
}
