package org.base.dto.exam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.model.exam.ExamModel;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamSubmitDTO {

    private String examCode;
    private ExamModel exam;
    private int executeTime;
}
