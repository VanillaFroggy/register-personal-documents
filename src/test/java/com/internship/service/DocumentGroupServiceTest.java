package com.internship.service;

import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.impl.DocumentGroupServiceImpl;
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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentGroupServiceTest {
    private static final long ID = 1L;
    private static final long USER_ID = 1L;
    private static final long OTHER_USER_ID = 2L;

    @InjectMocks
    private DocumentGroupServiceImpl documentGroupService;

    @Mock
    private DocumentGroupRepository groupRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ServiceMapper mapper;

    @Test
    void getAllDocumentGroups_shouldReturnAllDocumentGroups_whenExist() {
        List<DocumentGroup> documentGroups = List.of(mock(DocumentGroup.class));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(groupRepository.findAllByUserId(USER_ID)).thenReturn(documentGroups);
            when(mapper.toDto(documentGroups.getFirst())).thenReturn(mock(DocumentGroupDto.class));

            List<DocumentGroupDto> result = documentGroupService.getAllDocumentGroups();

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Test
    void getPageOfGroups_shouldReturnPageOfDocumentGroups_whenExist() {
        int pageNumber = 0, pageSize = 50;
        Page<DocumentGroup> documentGroups = new PageImpl<>(List.of(mock(DocumentGroup.class)));

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(groupRepository.findAllByUserId(USER_ID, PageRequest.of(pageNumber, pageSize))).thenReturn(documentGroups);
            when(mapper.toDto(documentGroups.getContent().getFirst())).thenReturn(mock(DocumentGroupDto.class));

            List<DocumentGroupDto> result = documentGroupService.getPageOfGroups(pageNumber, pageSize);

            assertNotNull(result);
            assertFalse(result.isEmpty());
        }
    }

    @Test
    void getGroupById_shouldReturnDocumentGroupDto_whenExists() throws AccessException, NotFoundException {
        DocumentGroup documentGroup = mock(DocumentGroup.class);
        DocumentGroupDto dto = mock(DocumentGroupDto.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(groupRepository.findById(ID)).thenReturn(Optional.of(documentGroup));
            when(documentGroup.getUser()).thenReturn(mock(User.class));
            when(documentGroup.getUser().getId()).thenReturn(USER_ID);
            when(mapper.toDto(documentGroup)).thenReturn(dto);
            when(dto.id()).thenReturn(ID);

            DocumentGroupDto result = documentGroupService.getGroupById(ID);

            assertNotNull(result);
            assertEquals(dto.id(), result.id());
        }
    }

    @Test
    void getGroupById_shouldThrowNotFoundException_whenDoesNotExist() {
        when(groupRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentGroupService.getGroupById(ID));
    }

    @Test
    void getGroupById_shouldThrowAccessException_whenUserDoesNotOwnDocumentGroup() {
        DocumentGroup documentGroup = mock(DocumentGroup.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(OTHER_USER_ID);
            when(groupRepository.findById(ID)).thenReturn(Optional.of(documentGroup));
            when(documentGroup.getUser()).thenReturn(mock(User.class));
            when(documentGroup.getUser().getId()).thenReturn(USER_ID);

            assertThrows(AccessException.class, () -> documentGroupService.getGroupById(ID));
        }
    }

    @Test
    void addGroup_shouldReturnDocumentGroupDto_whenValidRequest() throws NotFoundException {
        CreateDocumentGroupDto createDocumentGroupDto = mock(CreateDocumentGroupDto.class);
        DocumentGroup documentGroup = mock(DocumentGroup.class);
        DocumentGroupDto dto = mock(DocumentGroupDto.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(mapper.toEntity(createDocumentGroupDto)).thenReturn(documentGroup);
            when(mapper.toDto(documentGroup)).thenReturn(dto);
            when(userRepository.findById(USER_ID)).thenReturn(Optional.of(mock(User.class)));

            DocumentGroupDto actual = documentGroupService.addGroup(createDocumentGroupDto);

            assertNotNull(actual);
            assertEquals(dto.id(), actual.id());
        }
    }

    @Test
    void addGroup_shouldThrowNotFoundException_whenUserDoesNotExist() {
        CreateDocumentGroupDto createDocumentGroupDto = mock(CreateDocumentGroupDto.class);
        DocumentGroup documentGroup = mock(DocumentGroup.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(mapper.toEntity(createDocumentGroupDto)).thenReturn(documentGroup);
            when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> documentGroupService.addGroup(createDocumentGroupDto));
        }
    }

    @Test
    void updateGroup_shouldReturnDocumentGroupDto_whenValidRequest() throws AccessException, NotFoundException {
        UpdateDocumentGroupDto updateDocumentGroupDto = mock(UpdateDocumentGroupDto.class);
        DocumentGroup documentGroup = mock(DocumentGroup.class);
        DocumentGroupDto dto = mock(DocumentGroupDto.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(user.getId()).thenReturn(USER_ID);
            when(groupRepository.findById(updateDocumentGroupDto.id())).thenReturn(Optional.of(documentGroup));
            when(documentGroup.getUser()).thenReturn(user);
            when(mapper.toEntity(updateDocumentGroupDto)).thenReturn(documentGroup);
            when(mapper.toDto(documentGroup)).thenReturn(dto);

            DocumentGroupDto actual = documentGroupService.updateGroup(updateDocumentGroupDto);

            assertNotNull(actual);
            assertEquals(dto, actual);
        }
    }

    @Test
    void updateGroup_shouldThrowNotFoundException_whenDoesNotExist() {
        UpdateDocumentGroupDto updateDocumentGroupDto = mock(UpdateDocumentGroupDto.class);

        when(groupRepository.findById(updateDocumentGroupDto.id())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentGroupService.updateGroup(updateDocumentGroupDto));
    }

    @Test
    void updateGroup_shouldThrowAccessException_whenUserDoesNotOwnDocumentGroup() {
        UpdateDocumentGroupDto updateDocumentGroupDto = mock(UpdateDocumentGroupDto.class);
        DocumentGroup documentGroup = mock(DocumentGroup.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(OTHER_USER_ID);
            when(user.getId()).thenReturn(USER_ID);
            when(groupRepository.findById(updateDocumentGroupDto.id())).thenReturn(Optional.of(documentGroup));
            when(documentGroup.getUser()).thenReturn(user);

            assertThrows(AccessException.class, () -> documentGroupService.updateGroup(updateDocumentGroupDto));
        }
    }

    @Test
    void deleteGroup_shouldDeleteDocumentGroup_whenValidRequest() throws AccessException, NotFoundException {
        DocumentGroup documentGroup = mock(DocumentGroup.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(USER_ID);
            when(groupRepository.findById(ID)).thenReturn(Optional.of(documentGroup));
            when(documentGroup.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);

            documentGroupService.deleteGroup(ID);

            verify(groupRepository, times(1)).findById(ID);
            verify(groupRepository, times(1)).delete(documentGroup);
        }
    }

    @Test
    void deleteGroup_shouldThrowNotFoundException_whenDoesNotExist() {
        when(groupRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentGroupService.deleteGroup(ID));
    }

    @Test
    void deleteGroup_shouldThrowAccessException_whenUserDoesNotOwnDocumentGroup() {
        DocumentGroup documentGroup = mock(DocumentGroup.class);
        User user = mock(User.class);

        try (MockedStatic<Utils> mockedUtils = Mockito.mockStatic(Utils.class)) {
            mockedUtils.when(Utils::getCurrentUserId).thenReturn(OTHER_USER_ID);
            when(groupRepository.findById(ID)).thenReturn(Optional.of(documentGroup));
            when(documentGroup.getUser()).thenReturn(user);
            when(user.getId()).thenReturn(USER_ID);

            assertThrows(AccessException.class, () -> documentGroupService.deleteGroup(ID));
        }
    }
}