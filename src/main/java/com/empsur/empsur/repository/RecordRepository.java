package com.empsur.empsur.repository;

import com.empsur.empsur.domain.Record;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Record entity.
 */
@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {
    default Optional<Record> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Record> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Record> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct record from Record record left join fetch record.employee",
        countQuery = "select count(distinct record) from Record record"
    )
    Page<Record> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct record from Record record left join fetch record.employee")
    List<Record> findAllWithToOneRelationships();

    @Query("select record from Record record left join fetch record.employee where record.id =:id")
    Optional<Record> findOneWithToOneRelationships(@Param("id") Long id);
}
