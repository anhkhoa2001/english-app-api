package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "u_answer_attribute")
@Getter
@Setter
public class AnswerAttributeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "key")
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "item_id")
    @JsonIgnore
    private QuestionItemModel questionItemModel;
}
