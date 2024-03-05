package org.base.services;

import org.base.model.course.SectionModel;

public interface SectionService {

    SectionModel create(final SectionModel sectionModel);

    void deleteBySectionId(Integer sectionId);
}
