package com.internship.persistence.repo;

import com.internship.config.JpaTestConfig;
import com.internship.persistence.entity.DocumentType;
import com.internship.service.exceptoin.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaTestConfig.class})
class DocumentTypeRepositoryTest {
    private static final String NAME = "name";
    private static final int DAYS_BEFORE_EXPIRATION_TO_WARN_USER = 1;

    @Autowired
    private DocumentTypeRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
        repository.flush();
    }

    @Test
    void findByName_shouldFindDocumentType_whenExists() throws NotFoundException {
        final DocumentType documentType = new DocumentType();
        documentType.setName(NAME);
        documentType.setDaysBeforeExpirationToWarnUser(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);

        repository.save(documentType);
        final DocumentType actual = repository.findByName(NAME)
                .orElseThrow(NotFoundException::new);

        assertNotNull(actual);
        assertEquals(documentType, actual);
    }

    @Test
    void findByName_shouldFindNothing_whenDoesNotExist() {
        Optional<DocumentType> actual = repository.findByName(NAME);

        assertNotNull(actual);
        assertEquals(Optional.empty(), actual);
    }
}