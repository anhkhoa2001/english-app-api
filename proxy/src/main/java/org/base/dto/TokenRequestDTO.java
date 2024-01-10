package org.base.dto;

import lombok.Data;

@Data
public class TokenRequestDTO {
    private String username;
    private String code;
    private String type;
}
