package org.base.model.cache;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@RedisHash(value = "token_cache", timeToLive = 180000)
public class TokenCache {

    @Id
    private String code;
    private String username;
    private String token;
    private Long expiredIn;
    private Long createTime;
    private String type;


}
