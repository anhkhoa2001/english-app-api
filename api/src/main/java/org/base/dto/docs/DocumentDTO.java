package org.base.dto.docs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.dto.FileDTO;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocumentDTO {

    private Integer documentId;
    private String documentName;
    private String summary;
    private List<FileDTO> link;
    private List<FileDTO> thumbnail;
    private boolean status;
    private Set<String> skill;
    private Set<String> topic;
}
