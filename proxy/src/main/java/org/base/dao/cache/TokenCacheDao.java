package org.base.dao.cache;

import org.base.model.cache.TokenCache;

import java.util.List;

public interface TokenCacheDao {

    List<TokenCache> findByToken(String token);
}
