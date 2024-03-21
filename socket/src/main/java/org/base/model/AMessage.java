package org.base.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class AMessage {

    @Column(name = "ENTITY_REF")
    private String entityRef;

    @Column(name = "REF_ID")
    private String refId;

    @Column(name = "SENDER")
    private String sender;

    @Column(name = "SEND_TIME")
    private Date sendTime;

    @Column(name = "CONTENT")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getEntityRef() {
        return entityRef;
    }

    public void setEntityRef(String entityRef) {
        this.entityRef = entityRef;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
}
