package org.base.model.cache;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@RedisHash(value = "token_cache", timeToLive = 18000000)
public class TokenCache {

    @Id
    @Indexed
    private String code;
    @Indexed
    private String username;
    @Indexed
    private String token;
    private Long expiredIn;
    private Long createTime;
    private String type;
}
