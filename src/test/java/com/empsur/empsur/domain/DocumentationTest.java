package com.empsur.empsur.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.empsur.empsur.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DocumentationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Documentation.class);
        Documentation documentation1 = new Documentation();
        documentation1.setId(1L);
        Documentation documentation2 = new Documentation();
        documentation2.setId(documentation1.getId());
        assertThat(documentation1).isEqualTo(documentation2);
        documentation2.setId(2L);
        assertThat(documentation1).isNotEqualTo(documentation2);
        documentation1.setId(null);
        assertThat(documentation1).isNotEqualTo(documentation2);
    }
}
