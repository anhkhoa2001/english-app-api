package org.base.repositories.cache;

import org.base.model.cache.TokenCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenCacheRepository extends CrudRepository<TokenCache, String> {
    TokenCache findByUsernameAndType(String username, String type);
}
