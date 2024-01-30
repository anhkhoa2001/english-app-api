
package org.base.repositories.cache;

import org.base.dao.cache.TokenCacheDao;
import org.base.model.cache.TokenCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenCacheRepository extends CrudRepository<TokenCache, String>, TokenCacheDao {
    TokenCache findByUsernameAndType(String username, String type);

    List<TokenCache> findAllByToken(String token);

    List<TokenCache> findAllByUsername(String username);
}
