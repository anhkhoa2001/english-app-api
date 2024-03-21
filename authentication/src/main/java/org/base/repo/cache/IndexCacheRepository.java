package org.base.repo.cache;

import org.base.entity.cache.IndexCache;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndexCacheRepository extends CrudRepository<IndexCache, String> {
}
