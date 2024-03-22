package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Persistent;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "u_question_item")
@Getter
@Setter
public class QuestionItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Integer item_id;

    @Column(name = "HINT")
    private String hint;

    @Column(name = "INDEX")
    private Integer index;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "SOLUTION")
    private String solution;

    @Column(name = "TYPE")
    private String type;

    @OneToMany(mappedBy = "questionItemModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AnswerAttributeModel> answer;

    @ManyToOne
    @JoinColumn(name = "question_id")
    @JsonIgnore
    private QuestionModel questionModel;

    @Transient
    private String output;
}
