package com.empsur.empsur.repository;

import com.empsur.empsur.domain.Documentation;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DocumentationRepositoryWithBagRelationshipsImpl implements DocumentationRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Documentation> fetchBagRelationships(Optional<Documentation> documentation) {
        return documentation.map(this::fetchTags);
    }

    @Override
    public Page<Documentation> fetchBagRelationships(Page<Documentation> documentations) {
        return new PageImpl<>(
            fetchBagRelationships(documentations.getContent()),
            documentations.getPageable(),
            documentations.getTotalElements()
        );
    }

    @Override
    public List<Documentation> fetchBagRelationships(List<Documentation> documentations) {
        return Optional.of(documentations).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Documentation fetchTags(Documentation result) {
        return entityManager
            .createQuery(
                "select documentation from Documentation documentation left join fetch documentation.tags where documentation is :documentation",
                Documentation.class
            )
            .setParameter("documentation", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Documentation> fetchTags(List<Documentation> documentations) {
        return entityManager
            .createQuery(
                "select distinct documentation from Documentation documentation left join fetch documentation.tags where documentation in :documentations",
                Documentation.class
            )
            .setParameter("documentations", documentations)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
