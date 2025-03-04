package com.internship.service.impl;

import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.service.DocumentTypeService;
import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentTypeServiceImpl implements DocumentTypeService {
    private final DocumentTypeRepository documentTypeRepository;
    private final ServiceMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeDto> getAllDocumentTypes() {
        return documentTypeRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentTypeDto> getPageOfTypes(int pageNumber, int pageSize) {
        return documentTypeRepository.findAll(PageRequest.of(pageNumber, pageSize))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentTypeDto getTypeById(Long id) throws NotFoundException {
        return documentTypeRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NotFoundException::new);
    }

    @Override
    public DocumentTypeDto addType(CreateDocumentTypeDto dto) {
        DocumentType documentType = mapper.toEntity(dto);
        documentTypeRepository.save(documentType);
        return mapper.toDto(documentType);
    }

    @Override
    public DocumentTypeDto updateType(UpdateDocumentTypeDto dto) {
        DocumentType documentType = mapper.toEntity(dto);
        documentTypeRepository.save(documentType);
        return mapper.toDto(documentType);
    }

    @Override
    public void deleteType(Long id) {
        documentTypeRepository.deleteById(id);
    }
}
