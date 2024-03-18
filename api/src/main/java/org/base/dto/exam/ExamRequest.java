package org.base.dto.exam;

import lombok.Data;

import java.util.Set;

@Data
public class ExamRequest {

    private int page;
    private int pageSize;
    private Set<String> types;
    private Set<String> skills;
}
