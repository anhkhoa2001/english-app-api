package org.base.repositories.cache;

import org.base.model.cache.IndexCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexCacheRepository extends CrudRepository<IndexCache, String> {
}
