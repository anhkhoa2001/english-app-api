package org.base.oauth2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.base.dto.AItemDTO;

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookDTO extends AItemDTO {

    public static final String BASIC_NAME = "BookDTO";

    private String name;
}
