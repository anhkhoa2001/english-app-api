package org.base.model.exam;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "u_exam_part")
@Getter
@Setter
public class ExamPartModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PART_ID")
    private Integer partId;

    @ManyToOne
    @JoinColumn(name="EXAM_CODE", nullable=false)
    @JsonIgnore
    private ExamModel examModel;

    @OneToMany(mappedBy = "partModel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<QuestionModel> questions;

    @Column(name = "TYPE")
    private Integer type;
}
