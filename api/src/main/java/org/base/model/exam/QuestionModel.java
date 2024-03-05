package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "u_question")
@Getter
@Setter
public class QuestionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer questionId;

    @Column(name = "TYPE")
    private Integer type;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name="PART_ID", nullable=false)
    @JsonIgnore
    private ExamPartModel partModel;


}
