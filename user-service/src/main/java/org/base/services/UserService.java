package org.base.services;

import java.util.Map;

public interface UserService {

    String generateToken(Map<String, Object> bodyParam);
}
