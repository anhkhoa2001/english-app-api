package org.base.model.exam;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "u_exam_history")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "HISTORY_ID")
    private Integer historyId;

    @Column(name = "IMPLEMENTER")
    private String implementer;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "EXECUTE_TIME")
    private int executeTime;

    @Column(name = "JSON")
    private String json;

    @Column(name = "EXAM_CODE")
    private String examCode;

    @Column(name = "RESULT")
    private String result;

    @Column(name = "POINT")
    private int point;
}
