package org.base.model.cache;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Data
@RedisHash(value = "index_cache", timeToLive = 18000000)
public class IndexCache implements Serializable {

    @Id
    private String id;
    private int count;
    private Date createAt;
}
