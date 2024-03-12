package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "u_question")
@Getter
@Setter
public class QuestionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name="PART_ID", nullable=false)
    @JsonIgnore
    private ExamPartModel partModel;

    @OneToMany(mappedBy = "questionModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionItemModel> questionChilds;
}
