package org.base.services.impl;

import org.base.repositories.ExamPartRepository;
import org.base.services.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartServiceImpl implements PartService {

    @Autowired
    private ExamPartRepository examPartRepository;


}
