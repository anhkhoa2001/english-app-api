package org.base.dto.common;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.dto.AItemDTO;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequestDTO extends AItemDTO {

    private String requestMethod;
    private String requestPath;
    private Map<String, String> urlParam;
    private String pathParam;
    private Map<String, Object> bodyParam;
    private Map<String, String> headerParam;
    private boolean auth;
}
