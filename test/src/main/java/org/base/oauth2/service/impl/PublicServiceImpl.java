package org.base.oauth2.service.impl;

import org.base.exception.LogicException;
import org.base.exception.ValidationException;
import org.base.oauth2.service.PublicService;
import org.springframework.stereotype.Service;

@Service
public class PublicServiceImpl implements PublicService {

    @Override
    public Object handle(int number) {
        if(number == 500) {
            throw new LogicException("logic");
        } else if(number == 404) {
            throw new ValidationException("validate");
        } else {
            return "312";
        }
    }
}
