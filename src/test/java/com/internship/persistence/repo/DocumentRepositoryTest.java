package com.internship.persistence.repo;

import com.internship.config.JpaTestConfig;
import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {JpaTestConfig.class})
class DocumentRepositoryTest {
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String GROUP_NAME = "groupName";
    private static final String GROUP_COLOR = "groupColor";
    private static final String TYPE_NAME = "typeName";
    private static final int DAYS_BEFORE_EXPIRATION_TO_WARN_USER = 1;
    private static final String TITLE = "title";
    private static final ZonedDateTime DATE_OF_ISSUE = ZonedDateTime.now(ZoneOffset.UTC);
    private static final ZonedDateTime EXPIRATION_DATE = DATE_OF_ISSUE.plusDays(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentGroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DocumentTypeRepository typeRepository;

    @BeforeEach
    void setUp() {
        documentRepository.deleteAll();
        documentRepository.flush();
        groupRepository.deleteAll();
        groupRepository.flush();
        userRepository.deleteAll();
        userRepository.flush();
        typeRepository.deleteAll();
        typeRepository.flush();
    }

    @Test
    void findAllByUserId_shouldFindDocuments_whenExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        final DocumentGroup group = new DocumentGroup();
        group.setUser(user);
        group.setName(GROUP_NAME);
        group.setColor(GROUP_COLOR);
        final DocumentType type = new DocumentType();
        type.setName(TYPE_NAME);
        type.setDaysBeforeExpirationToWarnUser(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
        final Document document = new Document();
        document.setTitle(TITLE);
        document.setDateOfIssue(DATE_OF_ISSUE);
        document.setExpirationDate(EXPIRATION_DATE);
        document.setDocumentType(type);
        document.setDocumentGroup(group);
        document.setUser(user);

        userRepository.save(user);
        groupRepository.save(group);
        typeRepository.save(type);
        documentRepository.save(document);
        Page<Document> documents = documentRepository.findAllByUserId(
                user.getId(),
                PageRequest.of(0, 10)
        );

        assertFalse(documents.isEmpty());
        assertNotNull(documents.getContent().getFirst());
        assertEquals(document, documents.getContent().getFirst());
    }

    @Test
    void findAllByUserId_shouldFindNothing_whenDontExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        userRepository.save(user);
        Page<Document> documents = documentRepository.findAllByUserId(
                user.getId(),
                PageRequest.of(0, 10)
        );

        assertTrue(documents.isEmpty());
        assertTrue(documents.isLast());
    }

    @Test
    void findAllByUserIdAndDocumentGroupId_shouldFindDocuments_whenExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        final DocumentGroup group = new DocumentGroup();
        group.setUser(user);
        group.setName(GROUP_NAME);
        group.setColor(GROUP_COLOR);
        final DocumentType type = new DocumentType();
        type.setName(TYPE_NAME);
        type.setDaysBeforeExpirationToWarnUser(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
        final Document document = new Document();
        document.setTitle(TITLE);
        document.setDateOfIssue(DATE_OF_ISSUE);
        document.setExpirationDate(EXPIRATION_DATE);
        document.setDocumentType(type);
        document.setDocumentGroup(group);
        document.setUser(user);

        userRepository.save(user);
        groupRepository.save(group);
        typeRepository.save(type);
        documentRepository.save(document);
        Page<Document> documents = documentRepository.findAllByUserIdAndDocumentGroupId(
                user.getId(),
                group.getId(),
                PageRequest.of(0, 10)
        );

        assertFalse(documents.isEmpty());
        assertNotNull(documents.getContent().getFirst());
        assertEquals(document, documents.getContent().getFirst());
    }

    @Test
    void findAllByUserIdAndDocumentGroupId_shouldFindNothing_whenDontExist() {
        final User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        final DocumentGroup group = new DocumentGroup();
        group.setUser(user);
        group.setName(GROUP_NAME);
        group.setColor(GROUP_COLOR);
        final DocumentType type = new DocumentType();
        type.setName(TYPE_NAME);
        type.setDaysBeforeExpirationToWarnUser(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);

        userRepository.save(user);
        groupRepository.save(group);
        Page<Document> documents = documentRepository.findAllByUserIdAndDocumentGroupId(
                user.getId(),
                group.getId(),
                PageRequest.of(0, 10)
        );

        assertTrue(documents.isEmpty());
        assertTrue(documents.isLast());
    }
}