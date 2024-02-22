
package org.base.services;

import org.base.model.IndexModel;
import org.base.model.cache.IndexCache;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public interface AccessService {

    String getUrl(OAuth2User oAuth2User);

    String generateToken(Map<String, Object> bodyParam);

    void killToken(String token);

    IndexModel saveIndex(String code);

    IndexCache saveIndexCache(String id) throws InterruptedException;
}
