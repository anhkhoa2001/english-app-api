
package org.base.dao.impl;

import org.base.dao.TokenCacheDao;
import org.base.model.cache.TokenCache;
import org.springframework.stereotype.Repository;

import java.util.List;

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
