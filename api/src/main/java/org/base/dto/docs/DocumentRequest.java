package org.base.dto.docs;

import lombok.Data;

import java.util.Set;

@Data
public class DocumentRequest {

    private Integer page;
    private Integer pageSize;
    private Set<String> topic;
    private Set<String> skill;
}
