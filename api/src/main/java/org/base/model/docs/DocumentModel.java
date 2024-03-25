package org.base.model.docs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.base.dto.UserDTO;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "u_document")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DocumentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DOCUMENT_ID")
    private Integer documentId;

    @Column(name = "DOCUMENT_NAME")
    private String documentName;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "LINK")
    private String link;

    @Column(name = "SKILL")
    private String skill;

    @Column(name = "topic")
    private String topic;

    @Column(name = "STATUS")
    private boolean status;

    @Column(name = "CREATE_BY")
    private String createBy;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "SUMMARY")
    private String summary;

    @Column(name = "THUMBNAIL")
    private String thumbnail;

    @Transient
    private UserDTO author;
}
