package com.empsur.empsur.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Record.
 */
@Entity
@Table(name = "record")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnoreProperties(value = { "user", "documentations", "record", "company", "department" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Employee employee;

    @OneToMany(mappedBy = "record")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tags", "employee", "record" }, allowSetters = true)
    private Set<Documentation> documentations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Record id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Record name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Record employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Set<Documentation> getDocumentations() {
        return this.documentations;
    }

    public void setDocumentations(Set<Documentation> documentations) {
        if (this.documentations != null) {
            this.documentations.forEach(i -> i.setRecord(null));
        }
        if (documentations != null) {
            documentations.forEach(i -> i.setRecord(this));
        }
        this.documentations = documentations;
    }

    public Record documentations(Set<Documentation> documentations) {
        this.setDocumentations(documentations);
        return this;
    }

    public Record addDocumentation(Documentation documentation) {
        this.documentations.add(documentation);
        documentation.setRecord(this);
        return this;
    }

    public Record removeDocumentation(Documentation documentation) {
        this.documentations.remove(documentation);
        documentation.setRecord(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Record)) {
            return false;
        }
        return id != null && id.equals(((Record) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Record{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
