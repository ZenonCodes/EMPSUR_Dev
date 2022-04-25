package com.empsur.empsur.domain;

import com.empsur.empsur.domain.enumeration.DocumentationStatus;
import com.empsur.empsur.domain.enumeration.TaskStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Documentation.
 */
@Entity
@Table(name = "documentation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Documentation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DocumentationStatus status;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "attachment")
    private byte[] attachment;

    @Column(name = "attachment_content_type")
    private String attachmentContentType;

    @Column(name = "description")
    private String description;

    @Column(name = "issued")
    private LocalDate issued;

    @Column(name = "expiration")
    private LocalDate expiration;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval")
    private TaskStatus approval;

    @Column(name = "requested")
    private LocalDate requested;

    @ManyToMany
    @JoinTable(
        name = "rel_documentation__tag",
        joinColumns = @JoinColumn(name = "documentation_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "documentations" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "user", "documentations", "record", "company", "department" }, allowSetters = true)
    private Employee employee;

    @ManyToOne
    @JsonIgnoreProperties(value = { "employee", "documentations" }, allowSetters = true)
    private Record record;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Documentation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DocumentationStatus getStatus() {
        return this.status;
    }

    public Documentation status(DocumentationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DocumentationStatus status) {
        this.status = status;
    }

    public String getName() {
        return this.name;
    }

    public Documentation name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAttachment() {
        return this.attachment;
    }

    public Documentation attachment(byte[] attachment) {
        this.setAttachment(attachment);
        return this;
    }

    public void setAttachment(byte[] attachment) {
        this.attachment = attachment;
    }

    public String getAttachmentContentType() {
        return this.attachmentContentType;
    }

    public Documentation attachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
        return this;
    }

    public void setAttachmentContentType(String attachmentContentType) {
        this.attachmentContentType = attachmentContentType;
    }

    public String getDescription() {
        return this.description;
    }

    public Documentation description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getIssued() {
        return this.issued;
    }

    public Documentation issued(LocalDate issued) {
        this.setIssued(issued);
        return this;
    }

    public void setIssued(LocalDate issued) {
        this.issued = issued;
    }

    public LocalDate getExpiration() {
        return this.expiration;
    }

    public Documentation expiration(LocalDate expiration) {
        this.setExpiration(expiration);
        return this;
    }

    public void setExpiration(LocalDate expiration) {
        this.expiration = expiration;
    }

    public TaskStatus getApproval() {
        return this.approval;
    }

    public Documentation approval(TaskStatus approval) {
        this.setApproval(approval);
        return this;
    }

    public void setApproval(TaskStatus approval) {
        this.approval = approval;
    }

    public LocalDate getRequested() {
        return this.requested;
    }

    public Documentation requested(LocalDate requested) {
        this.setRequested(requested);
        return this;
    }

    public void setRequested(LocalDate requested) {
        this.requested = requested;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Documentation tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Documentation addTag(Tag tag) {
        this.tags.add(tag);
        tag.getDocumentations().add(this);
        return this;
    }

    public Documentation removeTag(Tag tag) {
        this.tags.remove(tag);
        tag.getDocumentations().remove(this);
        return this;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Documentation employee(Employee employee) {
        this.setEmployee(employee);
        return this;
    }

    public Record getRecord() {
        return this.record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public Documentation record(Record record) {
        this.setRecord(record);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Documentation)) {
            return false;
        }
        return id != null && id.equals(((Documentation) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Documentation{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", name='" + getName() + "'" +
            ", attachment='" + getAttachment() + "'" +
            ", attachmentContentType='" + getAttachmentContentType() + "'" +
            ", description='" + getDescription() + "'" +
            ", issued='" + getIssued() + "'" +
            ", expiration='" + getExpiration() + "'" +
            ", approval='" + getApproval() + "'" +
            ", requested='" + getRequested() + "'" +
            "}";
    }
}
