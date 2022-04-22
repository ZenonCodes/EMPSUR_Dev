package com.empsur.empsur.repository;

import com.empsur.empsur.domain.Documentation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DocumentationRepositoryWithBagRelationships {
    Optional<Documentation> fetchBagRelationships(Optional<Documentation> documentation);

    List<Documentation> fetchBagRelationships(List<Documentation> documentations);

    Page<Documentation> fetchBagRelationships(Page<Documentation> documentations);
}
