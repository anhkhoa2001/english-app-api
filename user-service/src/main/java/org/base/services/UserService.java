package org.base.services;

import java.util.Map;

public interface UserService {

    String generateToken(Map<String, Object> bodyParam);

    boolean checkToken(Map<String, String> headerParam);

    Object getAllUser();

    Object getUserInfo(Map<String, String> headerParam);
}
