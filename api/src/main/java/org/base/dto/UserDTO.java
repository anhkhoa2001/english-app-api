package org.base.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
