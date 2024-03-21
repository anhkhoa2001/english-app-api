
package org.base.dao;

import org.base.entity.cache.TokenCache;

import java.util.List;

public interface TokenCacheDao {

    List<TokenCache> findByToken(String token);
}
