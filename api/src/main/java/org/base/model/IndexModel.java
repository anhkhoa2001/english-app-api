package org.base.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "U_INDEX")
@Data
public class IndexModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "index")
    private Long index;

    @Column(name = "code", unique = true)
    private String code;
}
