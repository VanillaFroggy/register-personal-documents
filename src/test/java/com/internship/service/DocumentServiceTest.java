package com.internship.service;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.DocumentRepository;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.impl.DocumentServiceImpl;
import com.internship.service.mapper.ServiceMapper;
import com.internship.service.utils.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentServiceTest {
    private static final long ID = 1L;
    private static final long GROUP_ID = 1L;
    private static final long TYPE_ID = 1L;
    private static final long USER_ID = 1L;
    private static final long OTHER_USER_ID = 2L;
    private static final int DAYS_BEFORE_EXPIRATION_TO_WARN_USER = 1;
    private static final ZonedDateTime DATE_OF_ISSUE = ZonedDateTime.now(ZoneOffset.UTC);
    private static final ZonedDateTime EXPIRATION_DATE = DATE_OF_ISSUE.plusDays(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);

    @InjectMocks
    private DocumentServiceImpl documentService;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentGroupRepository groupRepository;

    @Mock
    private DocumentTypeRepository typeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceMapper mapper;

    @Test
    void hasDocumentsToRenew_shouldReturnTrue_whenExists() {
        int pageNumber = 0, pageSize = 100;
        Document document = mock(Document.class);
        DocumentType documentType = mock(DocumentType.class);
        Page<Document> page = new PageImpl<>(List.of(document));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(document.getDateOfIssue()).thenReturn(DATE_OF_ISSUE);
            when(document.getExpirationDate()).thenReturn(EXPIRATION_DATE);
            when(document.getDocumentType()).thenReturn(documentType);
            when(documentType.getDaysBeforeExpirationToWarnUser()).thenReturn(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize))).thenReturn(page);

            assertTrue(documentService.hasDocumentsToRenew());
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize));
        }
    }

    @Test
    void hasDocumentsToRenew_shouldReturnFalse_whenDontExist() {
        int pageNumber = 0, pageSize = 100;

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize)))
                    .thenReturn(Page.empty());
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber + 1, pageSize)))
                    .thenReturn(Page.empty());

            assertFalse(documentService.hasDocumentsToRenew());
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize));
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber + 1, pageSize));
        }
    }

    @Test
    void getPageOfDocumentsByGroup_shouldReturnListOfDocuments_whenExist() {
        int pageNumber = 0, pageSize = 50;
        Document document = mock(Document.class);
        DocumentDto dto = mock(DocumentDto.class);
        Page<Document> page = new PageImpl<>(List.of(document));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(documentRepository.findAllByUserIdAndDocumentGroupId(
                    USER_ID,
                    GROUP_ID,
                    PageRequest.of(pageNumber, pageSize)
            )).thenReturn(page);
            when(mapper.toDto(document)).thenReturn(dto);

            List<DocumentDto> actual = documentService.getPageOfDocumentsByGroup(GROUP_ID, pageNumber, pageSize);

            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertEquals(dto, actual.getFirst());
            verify(documentRepository, times(1))
                    .findAllByUserIdAndDocumentGroupId(USER_ID, GROUP_ID, PageRequest.of(pageNumber, pageSize));
        }
    }

    @Test
    void getPageOfDocumentsByGroup_shouldReturnEmptyList_whenDontExist() {
        int pageNumber = 0, pageSize = 50;

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(documentRepository.findAllByUserIdAndDocumentGroupId(
                    USER_ID,
                    GROUP_ID,
                    PageRequest.of(pageNumber, pageSize)
            )).thenReturn(Page.empty());

            List<DocumentDto> actual = documentService.getPageOfDocumentsByGroup(GROUP_ID, pageNumber, pageSize);

            assertNotNull(actual);
            assertTrue(actual.isEmpty());
            verify(documentRepository, times(1))
                    .findAllByUserIdAndDocumentGroupId(USER_ID, GROUP_ID, PageRequest.of(pageNumber, pageSize));
        }
    }

    @Test
    void getAllDocumentsToRenew_shouldReturnListOfDocuments_whenExist() {
        int pageNumber = 0, pageSize = 1_000;
        Document document = mock(Document.class);
        DocumentDto dto = mock(DocumentDto.class);
        DocumentType documentType = mock(DocumentType.class);
        Page<Document> page = new PageImpl<>(List.of(document));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(document.getDateOfIssue()).thenReturn(DATE_OF_ISSUE);
            when(document.getExpirationDate()).thenReturn(EXPIRATION_DATE);
            when(document.getDocumentType()).thenReturn(documentType);
            when(documentType.getDaysBeforeExpirationToWarnUser()).thenReturn(DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize))).thenReturn(page);
            when(mapper.toDto(document)).thenReturn(dto);
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber + 1, pageSize)))
                    .thenReturn(Page.empty());

            List<DocumentDto> actual = documentService.getAllDocumentsToRenew();

            assertNotNull(actual);
            assertFalse(actual.isEmpty());
            assertEquals(dto, actual.getFirst());
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize));
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber + 1, pageSize));
        }
    }

    @Test
    void getAllDocumentsToRenew_shouldReturnEmptyList_whenDontExist() {
        int pageNumber = 0, pageSize = 1_000;

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize)))
                    .thenReturn(Page.empty());
            when(documentRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber + 1, pageSize)))
                    .thenReturn(Page.empty());

            List<DocumentDto> actual = documentService.getAllDocumentsToRenew();

            assertNotNull(actual);
            assertTrue(actual.isEmpty());
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize));
            verify(documentRepository, times(1))
                    .findAllByUserId(USER_ID, PageRequest.of(pageNumber + 1, pageSize));
        }
    }

    @Test
    void getDocumentById_shouldReturnDocumentDto_whenExist() throws AccessException, NotFoundException {
        Document document = mock(Document.class);
        DocumentDto dto = mock(DocumentDto.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));
            when(mapper.toDto(document)).thenReturn(dto);

            DocumentDto actual = documentService.getDocumentById(ID);

            assertNotNull(actual);
            assertEquals(dto, actual);
            verify(documentRepository, times(1)).findById(ID);
        }
    }

    @Test
    void getDocumentById_shouldThrowNotFoundException_whenDoesNotExist() {
        when(documentRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentService.getDocumentById(ID));
        verify(documentRepository, times(1)).findById(ID);
    }

    @Test
    void getDocumentById_shouldThrowAccessException_whenUserDoesNotOwnDocument() {
        Document document = mock(Document.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(OTHER_USER_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));

            assertThrows(AccessException.class, () -> documentService.getDocumentById(ID));
            verify(documentRepository, times(1)).findById(ID);
        }
    }

    @Test
    void addDocument_shouldReturnDocumentDto_whenValidRequest() throws NotFoundException {
        CreateDocumentDto createDocumentDto = mock(CreateDocumentDto.class);
        Document document = mock(Document.class);
        DocumentGroup group = mock(DocumentGroup.class);
        DocumentType type = mock(DocumentType.class);
        User user = mock(User.class);
        DocumentDto dto = mock(DocumentDto.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(createDocumentDto.documentGroupId()).thenReturn(GROUP_ID);
            when(createDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
            when(groupRepository.findById(GROUP_ID)).thenReturn(Optional.of(group));
            when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.of(type));
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
            when(mapper.toEntity(createDocumentDto)).thenReturn(document);
            when(documentRepository.save(document)).thenReturn(document);
            when(mapper.toDto(document)).thenReturn(dto);

            DocumentDto actual = documentService.addDocument(createDocumentDto);

            assertNotNull(actual);
            assertEquals(dto, actual);
            verify(groupRepository, times(1)).findById(GROUP_ID);
            verify(typeRepository, times(1)).findById(TYPE_ID);
            verify(userRepository, times(1)).findById(USER_ID);
            verify(documentRepository, times(1)).save(any(Document.class));
        }
    }

    @Test
    void addDocument_shouldThrowNotFoundException_whenDocumentTypeDoesNotExist() {
        CreateDocumentDto createDocumentDto = mock(CreateDocumentDto.class);

        when(createDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
        when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentService.addDocument(createDocumentDto));
        verify(typeRepository, times(1)).findById(TYPE_ID);
    }

    @Test
    void addDocument_shouldThrowNotFoundException_whenDocumentGroupDoesNotExist() {
        CreateDocumentDto createDocumentDto = mock(CreateDocumentDto.class);
        Document document = mock(Document.class);
        DocumentType type = mock(DocumentType.class);

        when(createDocumentDto.documentGroupId()).thenReturn(GROUP_ID);
        when(createDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
        when(mapper.toEntity(createDocumentDto)).thenReturn(document);
        when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.of(type));
        when(groupRepository.findById(GROUP_ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentService.addDocument(createDocumentDto));
        verify(groupRepository, times(1)).findById(GROUP_ID);
        verify(typeRepository, times(1)).findById(TYPE_ID);
    }

    @Test
    void addDocument_shouldThrowNotFoundException_whenUserDoesNotExist() {
        CreateDocumentDto createDocumentDto = mock(CreateDocumentDto.class);
        Document document = mock(Document.class);
        DocumentGroup group = mock(DocumentGroup.class);
        DocumentType type = mock(DocumentType.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(createDocumentDto.documentGroupId()).thenReturn(GROUP_ID);
            when(createDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
            when(mapper.toEntity(createDocumentDto)).thenReturn(document);
            when(groupRepository.findById(GROUP_ID)).thenReturn(Optional.of(group));
            when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.of(type));
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> documentService.addDocument(createDocumentDto));
            verify(groupRepository, times(1)).findById(GROUP_ID);
            verify(typeRepository, times(1)).findById(TYPE_ID);
            verify(userRepository, times(1)).findById(USER_ID);
        }
    }

    @Test
    void updateDocument_shouldReturnDocumentDto_whenValidRequest() throws NotFoundException, AccessException {
        UpdateDocumentDto updateDocumentDto = mock(UpdateDocumentDto.class);
        Document document = mock(Document.class);
        DocumentGroup group = mock(DocumentGroup.class);
        DocumentType type = mock(DocumentType.class);
        User user = mock(User.class);
        DocumentDto dto = mock(DocumentDto.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(updateDocumentDto.id()).thenReturn(ID);
            when(updateDocumentDto.documentGroupId()).thenReturn(GROUP_ID);
            when(updateDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);
            when(mapper.toEntity(updateDocumentDto)).thenReturn(document);
            when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.of(type));
            when(groupRepository.findById(GROUP_ID)).thenReturn(Optional.of(group));
            when(mapper.toDto(document)).thenReturn(dto);

            DocumentDto actual = documentService.updateDocument(updateDocumentDto);

            assertNotNull(actual);
            assertEquals(dto, actual);
            verify(documentRepository, times(1)).findById(ID);
            verify(groupRepository, times(1)).findById(GROUP_ID);
            verify(typeRepository, times(1)).findById(TYPE_ID);
            verify(documentRepository, times(1)).save(document);
        }
    }

    @Test
    void updateDocument_shouldThrowNotFoundException_whenDocumentDoesNotExist() {
        UpdateDocumentDto updateDocumentDto = mock(UpdateDocumentDto.class);
        when(updateDocumentDto.id()).thenReturn(ID);
        when(documentRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentService.updateDocument(updateDocumentDto));
        verify(documentRepository, times(1)).findById(ID);
    }

    @Test
    void updateDocument_shouldThrowAccessException_whenUserDoesNotOwnDocument() {
        UpdateDocumentDto updateDocumentDto = mock(UpdateDocumentDto.class);
        Document document = mock(Document.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(updateDocumentDto.id()).thenReturn(ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(OTHER_USER_ID);

            assertThrows(AccessException.class, () -> documentService.updateDocument(updateDocumentDto));
            verify(documentRepository, times(1)).findById(ID);
        }
    }

    @Test
    void updateDocument_shouldThrowNotFoundException_whenDocumentTypeDoesNotExist() {
        UpdateDocumentDto updateDocumentDto = mock(UpdateDocumentDto.class);
        Document document = mock(Document.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(updateDocumentDto.id()).thenReturn(ID);
            when(updateDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);
            when(mapper.toEntity(updateDocumentDto)).thenReturn(document);
            when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> documentService.updateDocument(updateDocumentDto));
            verify(documentRepository, times(1)).findById(ID);
            verify(typeRepository, times(1)).findById(TYPE_ID);
        }
    }

    @Test
    void updateDocument_shouldThrowNotFoundException_whenDocumentGroupDoesNotExist() {
        UpdateDocumentDto updateDocumentDto = mock(UpdateDocumentDto.class);
        Document document = mock(Document.class);
        DocumentType type = mock(DocumentType.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(updateDocumentDto.id()).thenReturn(ID);
            when(updateDocumentDto.documentGroupId()).thenReturn(GROUP_ID);
            when(updateDocumentDto.documentTypeId()).thenReturn(TYPE_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);
            when(mapper.toEntity(updateDocumentDto)).thenReturn(document);
            when(typeRepository.findById(TYPE_ID)).thenReturn(Optional.of(type));
            when(groupRepository.findById(GROUP_ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> documentService.updateDocument(updateDocumentDto));
            verify(documentRepository, times(1)).findById(ID);
            verify(groupRepository, times(1)).findById(GROUP_ID);
            verify(typeRepository, times(1)).findById(TYPE_ID);
        }
    }

    @Test
    void deleteDocument_shouldDeleteDocument_whenValidRequest() throws AccessException, NotFoundException {
        Document document = mock(Document.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));

            documentService.deleteDocument(ID);

            verify(documentRepository, times(1)).findById(ID);
            verify(documentRepository, times(1)).delete(document);
        }
    }

    @Test
    void deleteDocument_shouldThrowNotFoundException_whenDoesNotExist() {
        when(documentRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentService.deleteDocument(ID));
    }

    @Test
    void deleteDocument_shouldThrowAccessException_whenUserDoesNotOwnDocument() {
        Document document = mock(Document.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(documentRepository.findById(ID)).thenReturn(Optional.of(document));
            when(document.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(OTHER_USER_ID);

            assertThrows(AccessException.class, () -> documentService.deleteDocument(ID));
            verify(documentRepository, times(1)).findById(ID);
        }
    }
}