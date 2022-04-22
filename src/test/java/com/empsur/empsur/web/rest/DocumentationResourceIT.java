package com.empsur.empsur.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.empsur.empsur.IntegrationTest;
import com.empsur.empsur.domain.Documentation;
import com.empsur.empsur.domain.enumeration.DocumentationStatus;
import com.empsur.empsur.domain.enumeration.TaskStatus;
import com.empsur.empsur.repository.DocumentationRepository;
import com.empsur.empsur.service.DocumentationService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link DocumentationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentationResourceIT {

    private static final DocumentationStatus DEFAULT_STATUS = DocumentationStatus.VALID;
    private static final DocumentationStatus UPDATED_STATUS = DocumentationStatus.INVALID;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final byte[] DEFAULT_ATTACHMENT = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ATTACHMENT = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ATTACHMENT_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ATTACHMENT_CONTENT_TYPE = "image/png";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ISSUED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ISSUED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_EXPIRATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_EXPIRATION = LocalDate.now(ZoneId.systemDefault());

    private static final TaskStatus DEFAULT_APPROVAL = TaskStatus.COMPLETED;
    private static final TaskStatus UPDATED_APPROVAL = TaskStatus.PENDING;

    private static final LocalDate DEFAULT_REQUESTED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REQUESTED = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/documentations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentationRepository documentationRepository;

    @Mock
    private DocumentationRepository documentationRepositoryMock;

    @Mock
    private DocumentationService documentationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentationMockMvc;

    private Documentation documentation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documentation createEntity(EntityManager em) {
        Documentation documentation = new Documentation()
            .status(DEFAULT_STATUS)
            .name(DEFAULT_NAME)
            .attachment(DEFAULT_ATTACHMENT)
            .attachmentContentType(DEFAULT_ATTACHMENT_CONTENT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .issued(DEFAULT_ISSUED)
            .expiration(DEFAULT_EXPIRATION)
            .approval(DEFAULT_APPROVAL)
            .requested(DEFAULT_REQUESTED);
        return documentation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documentation createUpdatedEntity(EntityManager em) {
        Documentation documentation = new Documentation()
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .issued(UPDATED_ISSUED)
            .expiration(UPDATED_EXPIRATION)
            .approval(UPDATED_APPROVAL)
            .requested(UPDATED_REQUESTED);
        return documentation;
    }

    @BeforeEach
    public void initTest() {
        documentation = createEntity(em);
    }

    @Test
    @Transactional
    void createDocumentation() throws Exception {
        int databaseSizeBeforeCreate = documentationRepository.findAll().size();
        // Create the Documentation
        restDocumentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isCreated());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeCreate + 1);
        Documentation testDocumentation = documentationList.get(documentationList.size() - 1);
        assertThat(testDocumentation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDocumentation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDocumentation.getAttachment()).isEqualTo(DEFAULT_ATTACHMENT);
        assertThat(testDocumentation.getAttachmentContentType()).isEqualTo(DEFAULT_ATTACHMENT_CONTENT_TYPE);
        assertThat(testDocumentation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentation.getIssued()).isEqualTo(DEFAULT_ISSUED);
        assertThat(testDocumentation.getExpiration()).isEqualTo(DEFAULT_EXPIRATION);
        assertThat(testDocumentation.getApproval()).isEqualTo(DEFAULT_APPROVAL);
        assertThat(testDocumentation.getRequested()).isEqualTo(DEFAULT_REQUESTED);
    }

    @Test
    @Transactional
    void createDocumentationWithExistingId() throws Exception {
        // Create the Documentation with an existing ID
        documentation.setId(1L);

        int databaseSizeBeforeCreate = documentationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isBadRequest());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentationRepository.findAll().size();
        // set the field null
        documentation.setStatus(null);

        // Create the Documentation, which fails.

        restDocumentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isBadRequest());

        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentationRepository.findAll().size();
        // set the field null
        documentation.setName(null);

        // Create the Documentation, which fails.

        restDocumentationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isBadRequest());

        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentations() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        // Get all the documentationList
        restDocumentationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentation.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].attachmentContentType").value(hasItem(DEFAULT_ATTACHMENT_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].attachment").value(hasItem(Base64Utils.encodeToString(DEFAULT_ATTACHMENT))))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].issued").value(hasItem(DEFAULT_ISSUED.toString())))
            .andExpect(jsonPath("$.[*].expiration").value(hasItem(DEFAULT_EXPIRATION.toString())))
            .andExpect(jsonPath("$.[*].approval").value(hasItem(DEFAULT_APPROVAL.toString())))
            .andExpect(jsonPath("$.[*].requested").value(hasItem(DEFAULT_REQUESTED.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(documentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(documentationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getDocumentation() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        // Get the documentation
        restDocumentationMockMvc
            .perform(get(ENTITY_API_URL_ID, documentation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentation.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.attachmentContentType").value(DEFAULT_ATTACHMENT_CONTENT_TYPE))
            .andExpect(jsonPath("$.attachment").value(Base64Utils.encodeToString(DEFAULT_ATTACHMENT)))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.issued").value(DEFAULT_ISSUED.toString()))
            .andExpect(jsonPath("$.expiration").value(DEFAULT_EXPIRATION.toString()))
            .andExpect(jsonPath("$.approval").value(DEFAULT_APPROVAL.toString()))
            .andExpect(jsonPath("$.requested").value(DEFAULT_REQUESTED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentation() throws Exception {
        // Get the documentation
        restDocumentationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDocumentation() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();

        // Update the documentation
        Documentation updatedDocumentation = documentationRepository.findById(documentation.getId()).get();
        // Disconnect from session so that the updates on updatedDocumentation are not directly saved in db
        em.detach(updatedDocumentation);
        updatedDocumentation
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .issued(UPDATED_ISSUED)
            .expiration(UPDATED_EXPIRATION)
            .approval(UPDATED_APPROVAL)
            .requested(UPDATED_REQUESTED);

        restDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDocumentation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDocumentation))
            )
            .andExpect(status().isOk());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
        Documentation testDocumentation = documentationList.get(documentationList.size() - 1);
        assertThat(testDocumentation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentation.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testDocumentation.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
        assertThat(testDocumentation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentation.getIssued()).isEqualTo(UPDATED_ISSUED);
        assertThat(testDocumentation.getExpiration()).isEqualTo(UPDATED_EXPIRATION);
        assertThat(testDocumentation.getApproval()).isEqualTo(UPDATED_APPROVAL);
        assertThat(testDocumentation.getRequested()).isEqualTo(UPDATED_REQUESTED);
    }

    @Test
    @Transactional
    void putNonExistingDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();
        documentation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();
        documentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();
        documentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentationWithPatch() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();

        // Update the documentation using partial update
        Documentation partialUpdatedDocumentation = new Documentation();
        partialUpdatedDocumentation.setId(documentation.getId());

        partialUpdatedDocumentation
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .expiration(UPDATED_EXPIRATION)
            .approval(UPDATED_APPROVAL)
            .requested(UPDATED_REQUESTED);

        restDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentation))
            )
            .andExpect(status().isOk());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
        Documentation testDocumentation = documentationList.get(documentationList.size() - 1);
        assertThat(testDocumentation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentation.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testDocumentation.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
        assertThat(testDocumentation.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testDocumentation.getIssued()).isEqualTo(DEFAULT_ISSUED);
        assertThat(testDocumentation.getExpiration()).isEqualTo(UPDATED_EXPIRATION);
        assertThat(testDocumentation.getApproval()).isEqualTo(UPDATED_APPROVAL);
        assertThat(testDocumentation.getRequested()).isEqualTo(UPDATED_REQUESTED);
    }

    @Test
    @Transactional
    void fullUpdateDocumentationWithPatch() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();

        // Update the documentation using partial update
        Documentation partialUpdatedDocumentation = new Documentation();
        partialUpdatedDocumentation.setId(documentation.getId());

        partialUpdatedDocumentation
            .status(UPDATED_STATUS)
            .name(UPDATED_NAME)
            .attachment(UPDATED_ATTACHMENT)
            .attachmentContentType(UPDATED_ATTACHMENT_CONTENT_TYPE)
            .description(UPDATED_DESCRIPTION)
            .issued(UPDATED_ISSUED)
            .expiration(UPDATED_EXPIRATION)
            .approval(UPDATED_APPROVAL)
            .requested(UPDATED_REQUESTED);

        restDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocumentation))
            )
            .andExpect(status().isOk());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
        Documentation testDocumentation = documentationList.get(documentationList.size() - 1);
        assertThat(testDocumentation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDocumentation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDocumentation.getAttachment()).isEqualTo(UPDATED_ATTACHMENT);
        assertThat(testDocumentation.getAttachmentContentType()).isEqualTo(UPDATED_ATTACHMENT_CONTENT_TYPE);
        assertThat(testDocumentation.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testDocumentation.getIssued()).isEqualTo(UPDATED_ISSUED);
        assertThat(testDocumentation.getExpiration()).isEqualTo(UPDATED_EXPIRATION);
        assertThat(testDocumentation.getApproval()).isEqualTo(UPDATED_APPROVAL);
        assertThat(testDocumentation.getRequested()).isEqualTo(UPDATED_REQUESTED);
    }

    @Test
    @Transactional
    void patchNonExistingDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();
        documentation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();
        documentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentation))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentation() throws Exception {
        int databaseSizeBeforeUpdate = documentationRepository.findAll().size();
        documentation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documentation in the database
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentation() throws Exception {
        // Initialize the database
        documentationRepository.saveAndFlush(documentation);

        int databaseSizeBeforeDelete = documentationRepository.findAll().size();

        // Delete the documentation
        restDocumentationMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Documentation> documentationList = documentationRepository.findAll();
        assertThat(documentationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
