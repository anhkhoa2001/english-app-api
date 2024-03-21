
package org.base.repo.cache;

import org.base.entity.cache.TokenCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenCacheRepository extends CrudRepository<TokenCache, String> {
    TokenCache findByUsernameAndType(String username, String type);

    List<TokenCache> findAllByToken(String token);

    List<TokenCache> findAllByUsername(String username);
}
