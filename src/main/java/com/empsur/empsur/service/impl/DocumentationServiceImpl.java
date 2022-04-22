package com.empsur.empsur.service.impl;

import com.empsur.empsur.domain.Documentation;
import com.empsur.empsur.repository.DocumentationRepository;
import com.empsur.empsur.security.AuthoritiesConstants;
import com.empsur.empsur.security.SecurityUtils;
import com.empsur.empsur.service.DocumentationService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Documentation}.
 */
@Service
@Transactional
public class DocumentationServiceImpl implements DocumentationService {

    private final Logger log = LoggerFactory.getLogger(DocumentationServiceImpl.class);

    private final DocumentationRepository documentationRepository;

    public DocumentationServiceImpl(DocumentationRepository documentationRepository) {
        this.documentationRepository = documentationRepository;
    }

    @Override
    public Documentation save(Documentation documentation) {
        log.debug("Request to save Documentation : {}", documentation);
        return documentationRepository.save(documentation);
    }

    @Override
    public Documentation update(Documentation documentation) {
        log.debug("Request to save Documentation : {}", documentation);
        return documentationRepository.save(documentation);
    }

    @Override
    public Optional<Documentation> partialUpdate(Documentation documentation) {
        log.debug("Request to partially update Documentation : {}", documentation);

        return documentationRepository
            .findById(documentation.getId())
            .map(existingDocumentation -> {
                if (documentation.getStatus() != null) {
                    existingDocumentation.setStatus(documentation.getStatus());
                }
                if (documentation.getName() != null) {
                    existingDocumentation.setName(documentation.getName());
                }
                if (documentation.getAttachment() != null) {
                    existingDocumentation.setAttachment(documentation.getAttachment());
                }
                if (documentation.getAttachmentContentType() != null) {
                    existingDocumentation.setAttachmentContentType(documentation.getAttachmentContentType());
                }
                if (documentation.getDescription() != null) {
                    existingDocumentation.setDescription(documentation.getDescription());
                }
                if (documentation.getIssued() != null) {
                    existingDocumentation.setIssued(documentation.getIssued());
                }
                if (documentation.getExpiration() != null) {
                    existingDocumentation.setExpiration(documentation.getExpiration());
                }
                if (documentation.getApproval() != null) {
                    existingDocumentation.setApproval(documentation.getApproval());
                }
                if (documentation.getRequested() != null) {
                    existingDocumentation.setRequested(documentation.getRequested());
                }

                return existingDocumentation;
            })
            .map(documentationRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Documentation> findAll(Pageable pageable) {
        log.debug("Request to get all Documentations");
            return documentationRepository.findAll(pageable);
    }

    public Page<Documentation> findAllWithEagerRelationships(Pageable pageable) {
        return documentationRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Documentation> findOne(Long id) {
        log.debug("Request to get Documentation : {}", id);
        return documentationRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Documentation : {}", id);
        documentationRepository.deleteById(id);
    }
}
