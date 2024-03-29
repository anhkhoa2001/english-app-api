package org.base.dto.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.base.dto.AItemDTO;
import org.base.utils.StringUtil;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageResponseDTO<T> extends AItemDTO {

    public static final String BASIC_NAME = "MessageResponseDTO";

    private int status;
    private String message;
    private String path;
    private String timestamp;
    private T data;
    private int total;

    public MessageResponseDTO(int status, String message, T data, String path) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.path = path;
        super.setCode(UUID.randomUUID().toString());
        this.timestamp = StringUtil.generatorCurrentDateTime();
    }

    public MessageResponseDTO() {
        this.status = HttpStatus.OK.value();
        this.message = HttpStatus.OK.getReasonPhrase();
        super.setCode(UUID.randomUUID().toString());
        this.timestamp = StringUtil.generatorCurrentDateTime();
    }

    public MessageResponseDTO build(Object obj) {
        MessageResponseDTO msg = new MessageResponseDTO();
        if (obj instanceof MessageResponseDTO) {
            msg = (MessageResponseDTO) obj;
        } else {
            msg.setData(obj);
        }
        if (msg.getTimestamp() == null) {
            msg.setTimestamp(StringUtil.generatorCurrentDateTime());
        }
        if (msg.getCode() == null) {
            msg.setCode(UUID.randomUUID().toString());
        }
        if (msg.getData() instanceof List) {
            msg.setTotal((((List<?>) msg.getData()).size()));
        } else if (msg.getData() instanceof Map) {
            msg.setTotal((((Map) msg.getData()).size()));
        }

        return msg;
    }

    public String getBasicName() {
        return BASIC_NAME;
    }
}
