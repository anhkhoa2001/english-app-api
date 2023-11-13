package org.base.config;

import org.base.dto.common.MessageResponseDTO;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

public class StringToMessageResponseDTOConverter extends MappingJackson2HttpMessageConverter {

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {

        if (object instanceof String) {
            // Nếu object là String, wrap nó thành MessageResponseDTO
            MessageResponseDTO messageResponseDTO = new MessageResponseDTO();
            messageResponseDTO.setMessage((String) object);
            object = messageResponseDTO;
        }

        super.writeInternal(object, outputMessage);
    }

}