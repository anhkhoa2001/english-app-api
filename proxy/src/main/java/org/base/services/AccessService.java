package org.base.services;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

public interface AccessService {

    String getUrl(OAuth2User oAuth2User);

    String generateToken(Map<String, Object> bodyParam);

    void killToken(String token);
}
