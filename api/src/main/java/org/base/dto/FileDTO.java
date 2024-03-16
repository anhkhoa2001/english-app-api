package org.base.dto;

import lombok.Data;

import java.util.Map;

@Data
public class FileDTO {

    private String name;
    private Map<String, Object> response;
    private long size;
    private String uid;
    private String type;
    private long duration;
}
