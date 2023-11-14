package org.base.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "E_USER")
public class UserModel {

    @Id
    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FULLNAME")
    private String fullname;

    @Column(name = "AVATAR")
    private String avatar;
}
