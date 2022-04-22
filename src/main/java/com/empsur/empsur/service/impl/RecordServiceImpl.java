package com.empsur.empsur.service.impl;

import com.empsur.empsur.domain.Record;
import com.empsur.empsur.repository.RecordRepository;
import com.empsur.empsur.service.RecordService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Record}.
 */
@Service
@Transactional
public class RecordServiceImpl implements RecordService {

    private final Logger log = LoggerFactory.getLogger(RecordServiceImpl.class);

    private final RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public Record save(Record record) {
        log.debug("Request to save Record : {}", record);
        return recordRepository.save(record);
    }

    @Override
    public Record update(Record record) {
        log.debug("Request to save Record : {}", record);
        return recordRepository.save(record);
    }

    @Override
    public Optional<Record> partialUpdate(Record record) {
        log.debug("Request to partially update Record : {}", record);

        return recordRepository
            .findById(record.getId())
            .map(existingRecord -> {
                if (record.getName() != null) {
                    existingRecord.setName(record.getName());
                }

                return existingRecord;
            })
            .map(recordRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Record> findAll(Pageable pageable) {
        log.debug("Request to get all Records");
        return recordRepository.findAll(pageable);
    }

    public Page<Record> findAllWithEagerRelationships(Pageable pageable) {
        return recordRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Record> findOne(Long id) {
        log.debug("Request to get Record : {}", id);
        return recordRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Record : {}", id);
        recordRepository.deleteById(id);
    }
}
