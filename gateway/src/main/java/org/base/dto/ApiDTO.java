package org.base.dto;

import lombok.Data;

@Data
public class ApiDTO {

    private String path;
    private String topic;
    //true la can token
    //false la khong can
    private boolean auth;
    private String method;
}
