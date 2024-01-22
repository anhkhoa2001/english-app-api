package org.base.dao.cache.impl;

import org.base.dao.cache.TokenCacheDao;
import org.base.model.cache.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class TokenCacheDaoImpl implements TokenCacheDao {

    /*@Autowired
    private HashOperations<String, String, TokenCache> hashOperations;

    @Autowired
    private SetOperations<String, String> setOperations;;*/

    @Override
    public List<TokenCache> findByToken(String token) {
        return null;
    }
}
