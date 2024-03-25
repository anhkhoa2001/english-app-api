package org.base.services;

import org.base.dto.docs.DocumentDTO;
import org.base.dto.docs.DocumentRequest;
import org.base.model.docs.DocumentModel;

public interface DocumentService {

    DocumentModel create(DocumentDTO request, String token);

    Object getAll();

    DocumentModel update(DocumentDTO request);

    Object getAllPublic(DocumentRequest request);
}
