package org.base.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogRequest {

    private Set<String> skill;
    private Set<String> englishFor;
    private Set<String> englishBasic;
    private Integer page;
    private Integer pageSize;
}
