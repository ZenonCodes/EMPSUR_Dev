package com.empsur.empsur.repository;

import com.empsur.empsur.domain.Documentation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Documentation entity.
 */
@Repository
public interface DocumentationRepository extends DocumentationRepositoryWithBagRelationships, JpaRepository<Documentation, Long> {
    default Optional<Documentation> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Documentation> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Documentation> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select distinct documentation from Documentation documentation left join fetch documentation.employee left join fetch documentation.record",
        countQuery = "select count(distinct documentation) from Documentation documentation"
    )
    Page<Documentation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct documentation from Documentation documentation left join fetch documentation.employee left join fetch documentation.record"
    )
    List<Documentation> findAllWithToOneRelationships();

    @Query(
        "select documentation from Documentation documentation left join fetch documentation.employee left join fetch documentation.record where documentation.id =:id"
    )
    Optional<Documentation> findOneWithToOneRelationships(@Param("id") Long id);

    Page<Documentation> findByEmployeeUserLogin(String userLogin, Pageable pageable);
}
