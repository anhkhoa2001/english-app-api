package org.base.model;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "E_USER")
public class UserModel {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CREATE_AT")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FULLNAME")
    private Course fullname;

    @Column(name = "AVATAR")
    private String avatar;

    @Column(name = "TYPE")
    private String type;
}
