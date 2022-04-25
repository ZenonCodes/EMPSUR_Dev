package com.empsur.empsur.web.rest;

import com.empsur.empsur.domain.Documentation;
import com.empsur.empsur.repository.DocumentationRepository;
import com.empsur.empsur.service.DocumentationService;
import com.empsur.empsur.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.empsur.empsur.domain.Documentation}.
 */
@RestController
@RequestMapping("/api")
public class DocumentationResource {

    private final Logger log = LoggerFactory.getLogger(DocumentationResource.class);

    private static final String ENTITY_NAME = "documentation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DocumentationService documentationService;

    private final DocumentationRepository documentationRepository;

    public DocumentationResource(DocumentationService documentationService, DocumentationRepository documentationRepository) {
        this.documentationService = documentationService;
        this.documentationRepository = documentationRepository;
    }

    /**
     * {@code POST  /documentations} : Create a new documentation.
     *
     * @param documentation the documentation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new documentation, or with status {@code 400 (Bad Request)} if the documentation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/documentations")
    public ResponseEntity<Documentation> createDocumentation(@Valid @RequestBody Documentation documentation) throws URISyntaxException {
        log.debug("REST request to save Documentation : {}", documentation);
        if (documentation.getId() != null) {
            throw new BadRequestAlertException("A new documentation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Documentation result = documentationService.save(documentation);
        return ResponseEntity
            .created(new URI("/api/documentations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /documentations/:id} : Updates an existing documentation.
     *
     * @param id the id of the documentation to save.
     * @param documentation the documentation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentation,
     * or with status {@code 400 (Bad Request)} if the documentation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the documentation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/documentations/{id}")
    public ResponseEntity<Documentation> updateDocumentation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Documentation documentation
    ) throws URISyntaxException {
        log.debug("REST request to update Documentation : {}, {}", id, documentation);
        if (documentation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Documentation result = documentationService.update(documentation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /documentations/:id} : Partial updates given fields of an existing documentation, field will ignore if it is null
     *
     * @param id the id of the documentation to save.
     * @param documentation the documentation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated documentation,
     * or with status {@code 400 (Bad Request)} if the documentation is not valid,
     * or with status {@code 404 (Not Found)} if the documentation is not found,
     * or with status {@code 500 (Internal Server Error)} if the documentation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/documentations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Documentation> partialUpdateDocumentation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Documentation documentation
    ) throws URISyntaxException {
        log.debug("REST request to partial update Documentation partially : {}, {}", id, documentation);
        if (documentation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, documentation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!documentationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Documentation> result = documentationService.partialUpdate(documentation);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, documentation.getId().toString())
        );
    }

    /**
     * {@code GET  /documentations} : get all the documentations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of documentations in body.
     */
    @GetMapping("/documentations")
    public ResponseEntity<List<Documentation>> getAllDocumentations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Documentations");
        Page<Documentation> page;
        if (eagerload) {
            page = documentationService.findAllWithEagerRelationships(pageable);
        } else {
            page = documentationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /documentations/:id} : get the "id" documentation.
     *
     * @param id the id of the documentation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the documentation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/documentations/{id}")
    public ResponseEntity<Documentation> getDocumentation(@PathVariable Long id) {
        log.debug("REST request to get Documentation : {}", id);
        Optional<Documentation> documentation = documentationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(documentation);
    }

    /**
     * {@code DELETE  /documentations/:id} : delete the "id" documentation.
     *
     * @param id the id of the documentation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/documentations/{id}")
    public ResponseEntity<Void> deleteDocumentation(@PathVariable Long id) {
        log.debug("REST request to delete Documentation : {}", id);
        documentationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
