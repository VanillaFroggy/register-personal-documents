package com.internship.service;

import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.impl.DocumentTypeServiceImpl;
import com.internship.service.mapper.ServiceMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DocumentTypeServiceTest {
    private static final long ID= 1L;

    @InjectMocks
    private DocumentTypeServiceImpl documentTypeService;

    @Mock
    private DocumentTypeRepository typeRepository;

    @Mock
    private ServiceMapper mapper;

    @Test
    void getAllDocumentTypes_shouldReturnAllDocumentTypes_whenExist() {
        List<DocumentType> documentTypes = List.of(mock(DocumentType.class), mock(DocumentType.class));

        when(typeRepository.findAll()).thenReturn(documentTypes);
        when(mapper.toDto(documentTypes.getFirst())).thenReturn(mock(DocumentTypeDto.class));

        List<DocumentTypeDto> result = documentTypeService.getAllDocumentTypes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getPageOfDocumentTypes_shouldReturnPageOfDocumentTypes_whenExist() {
        int pageNumber = 0, pageSize = 10;
        Page<DocumentType> documentTypes = new PageImpl<>(List.of(mock(DocumentType.class), mock(DocumentType.class)));

        when(typeRepository.findAll(PageRequest.of(pageNumber, pageSize))).thenReturn(documentTypes);
        when(mapper.toDto(documentTypes.getContent().getFirst())).thenReturn(mock(DocumentTypeDto.class));

        List<DocumentTypeDto> result = documentTypeService.getPageOfTypes(pageNumber, pageSize);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void getTypeById_shouldReturnDocumentTypeDto_whenExists() throws NotFoundException {
        DocumentType documentType = mock(DocumentType.class);
        DocumentTypeDto dto = mock(DocumentTypeDto.class);

        when(typeRepository.findById(ID)).thenReturn(Optional.of(documentType));
        when(mapper.toDto(documentType)).thenReturn(dto);
        when(dto.id()).thenReturn(ID);

        DocumentTypeDto result = documentTypeService.getTypeById(ID);

        assertNotNull(result);
        assertEquals(dto.id(), result.id());
    }

    @Test
    void getTypeById_shouldThrowNotFoundException_whenDoesNotExist() {
        when(typeRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentTypeService.getTypeById(ID));
    }

    @Test
    void addType_shouldReturnDocumentTypeDto_whenValidRequest() {
        CreateDocumentTypeDto createDocumentTypeDto = mock(CreateDocumentTypeDto.class);
        DocumentType documentType = mock(DocumentType.class);
        DocumentTypeDto dto = mock(DocumentTypeDto.class);

        when(mapper.toEntity(createDocumentTypeDto)).thenReturn(documentType);
        when(typeRepository.save(documentType)).thenReturn(documentType);
        when(mapper.toDto(documentType)).thenReturn(dto);
        when(dto.id()).thenReturn(ID);

        DocumentTypeDto result = documentTypeService.addType(createDocumentTypeDto);

        assertNotNull(result);
        assertEquals(dto.id(), result.id());
    }

    @Test
    void updateType_shouldReturnDocumentTypeDto_whenValidRequest() throws NotFoundException {
        UpdateDocumentTypeDto updateDocumentTypeDto = mock(UpdateDocumentTypeDto.class);
        DocumentType documentType = mock(DocumentType.class);
        DocumentTypeDto dto = mock(DocumentTypeDto.class);

        when(updateDocumentTypeDto.id()).thenReturn(ID);
        when(typeRepository.findById(ID)).thenReturn(Optional.of(documentType));
        when(mapper.toEntity(updateDocumentTypeDto)).thenReturn(documentType);
        when(typeRepository.save(documentType)).thenReturn(documentType);
        when(mapper.toDto(documentType)).thenReturn(dto);
        when(dto.id()).thenReturn(ID);

        DocumentTypeDto result = documentTypeService.updateType(updateDocumentTypeDto);

        assertNotNull(result);
        assertEquals(dto.id(), result.id());
    }

    @Test
    void updateType_shouldThrowNotFoundException_whenDoesNotExist() {
        UpdateDocumentTypeDto updateDocumentTypeDto = mock(UpdateDocumentTypeDto.class);

        when(updateDocumentTypeDto.id()).thenReturn(ID);
        when(typeRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> documentTypeService.updateType(updateDocumentTypeDto));
    }

    @Test
    void deleteType_shouldDeleteDocumentType_whenValidRequest() {
        documentTypeService.deleteType(ID);

        verify(typeRepository, times(1)).deleteById(ID);
    }
}