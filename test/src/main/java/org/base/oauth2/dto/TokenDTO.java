package org.base.oauth2.dto;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.base.utils.Constants;

import java.util.Date;

@Slf4j
@Data
public class TokenDTO {

    private String access_token;
    private Long time_live;
    private Date time_create;
    private String token_type;

    public TokenDTO(String access_token, Long time_live) {
        this.access_token = access_token;
        this.time_create = new Date();
        this.time_live = time_live;
        this.token_type = Constants.TOKEN_TYPE;
    }
}
