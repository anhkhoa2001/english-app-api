package org.base.dto.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.dto.FileDTO;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlogDTO {

    private Integer blogId;

    private List<FileDTO> thumbnail;

    private String title;

    private String summary;

    private Set<String> skill;

    private Set<String> englishBasic;

    private Set<String> englishFor;

    private String content;

    private boolean status;
}
