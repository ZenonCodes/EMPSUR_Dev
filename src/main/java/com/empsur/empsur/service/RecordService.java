package com.empsur.empsur.service;

import com.empsur.empsur.domain.Record;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Record}.
 */
public interface RecordService {
    /**
     * Save a record.
     *
     * @param record the entity to save.
     * @return the persisted entity.
     */
    Record save(Record record);

    /**
     * Updates a record.
     *
     * @param record the entity to update.
     * @return the persisted entity.
     */
    Record update(Record record);

    /**
     * Partially updates a record.
     *
     * @param record the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Record> partialUpdate(Record record);

    /**
     * Get all the records.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Record> findAll(Pageable pageable);

    /**
     * Get all the records with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Record> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" record.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Record> findOne(Long id);

    /**
     * Delete the "id" record.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
