package org.base.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserDTO {

    private String userId;
    private Date createAt;
    private String username;
    private String email;
    private String fullname;
    private String avatar;
    private String type;
    private String roleCode;
}
