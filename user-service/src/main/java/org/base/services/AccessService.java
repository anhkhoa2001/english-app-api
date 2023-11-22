package org.base.services;

import org.springframework.security.oauth2.core.user.OAuth2User;

public interface AccessService {

    String getUrl(OAuth2User oAuth2User);
}
