package com.empsur.empsur.service;

import com.empsur.empsur.domain.Documentation;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Documentation}.
 */
public interface DocumentationService {
    /**
     * Save a documentation.
     *
     * @param documentation the entity to save.
     * @return the persisted entity.
     */
    Documentation save(Documentation documentation);

    /**
     * Updates a documentation.
     *
     * @param documentation the entity to update.
     * @return the persisted entity.
     */
    Documentation update(Documentation documentation);

    /**
     * Partially updates a documentation.
     *
     * @param documentation the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Documentation> partialUpdate(Documentation documentation);

    /**
     * Get all the documentations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Documentation> findAll(Pageable pageable);

    /**
     * Get all the documentations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Documentation> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" documentation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Documentation> findOne(Long id);

    /**
     * Delete the "id" documentation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
