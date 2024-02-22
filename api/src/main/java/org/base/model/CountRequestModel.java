package org.base.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "COUNT_REQUEST")
@Data
public class CountRequestModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rowId;

    @Basic
    private Date createTime;

    @Basic
    private int count;

    @Basic
    private String request;

    @Basic
    private String response;
}
