package org.base.dto;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;


@Data
public abstract class AItemDTO {

    public static final String BASIC_NAME = "AItemDTO";

    private String code;

}
