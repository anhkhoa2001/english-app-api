package org.base.dto.common;

import lombok.Data;
import org.base.utils.Constants;

@Data
public class ModuleErrorResponseDTO {

    private String message;
    private int code;
    private String checkPoint;

    public ModuleErrorResponseDTO(String message, int code) {
        this.message = message;
        this.code = code;
        this.checkPoint = Constants.TEMPLE_SPLIT;
    }
}
