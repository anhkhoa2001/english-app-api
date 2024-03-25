package org.base.services.impl;

import org.base.config.JwtTokenSetup;
import org.base.dto.docs.DocumentDTO;
import org.base.dto.docs.DocumentRequest;
import org.base.exception.SystemException;
import org.base.exception.ValidationException;
import org.base.model.docs.DocumentModel;
import org.base.repositories.DocumentRepository;
import org.base.services.DocumentService;
import org.base.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final JwtTokenSetup jwtTokenSetup;

    @Autowired
    public DocumentServiceImpl(DocumentRepository documentRepository, JwtTokenSetup jwtTokenSetup) {
        this.documentRepository = documentRepository;
        this.jwtTokenSetup = jwtTokenSetup;
    }


    @Override
    public DocumentModel create(DocumentDTO request, String token) {
        try {
            String userId = jwtTokenSetup.getUserIdFromToken(token);
            DocumentModel document = new DocumentModel();
            document.setCreateAt(new Date());
            document.setCreateBy(userId);
            document.setDocumentName(request.getDocumentName());
            document.setStatus(request.isStatus());
            document.setSummary(request.getSummary());

            if(!StringUtil.isListEmpty(request.getThumbnail())) {
                String image = (String) request.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(request.getThumbnail().get(0).getType().contains("image/")) {
                    document.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            if(!StringUtil.isListEmpty(request.getLink())) {
                String link = (String) request.getLink().get(0).getResponse()
                        .getOrDefault("default", null);
                document.setLink(link);
                document.setType(request.getLink().get(0).getType());
            }
            document.setTopic(String.join(",", request.getTopic()));
            document.setSkill(String.join(",", request.getSkill()));

            document = documentRepository.save(document);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("create document failed!!");
        }
    }

    @Override
    public Object getAll() {
        return documentRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
    }

    @Override
    public DocumentModel update(DocumentDTO request) {
        try {
            Optional<DocumentModel> opDocument = documentRepository.findById(request.getDocumentId());

            if(opDocument.isEmpty()) {
                throw new SystemException("document not exist!!");
            }
            DocumentModel document = opDocument.get();
            document.setDocumentName(request.getDocumentName());
            document.setStatus(request.isStatus());
            document.setSummary(request.getSummary());

            if(!StringUtil.isListEmpty(request.getThumbnail())) {
                String image = (String) request.getThumbnail().get(0).getResponse()
                        .getOrDefault("default", null);
                if(request.getThumbnail().get(0).getType().contains("image/")) {
                    document.setThumbnail(image);
                } else {
                    throw new ValidationException("Type image invalid!!");
                }
            }

            if(!StringUtil.isListEmpty(request.getLink())) {
                String link = (String) request.getLink().get(0).getResponse()
                        .getOrDefault("default", null);
                document.setLink(link);
                document.setType(request.getLink().get(0).getType());
            }
            document.setTopic(String.join(",", request.getTopic()));
            document.setSkill(String.join(",", request.getSkill()));

            document = documentRepository.save(document);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            throw new SystemException("update document failed!!");
        }
    }

    @Override
    public Object getAllPublic(DocumentRequest request) {
        return documentRepository.getAllPublic(request);
    }
}
