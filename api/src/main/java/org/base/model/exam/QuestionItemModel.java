package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "u_question_item")
@Getter
@Setter
public class QuestionItemModel {

    @Id
    @Column(name = "answer_id")
    private Integer answer_id;

    @Column(name = "HINT")
    private String hint;

    @Column(name = "INDEX")
    private Integer index;

    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "questionItemModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnswerAttributeModel> answers;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private QuestionModel questionModel;
}
