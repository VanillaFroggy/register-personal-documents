package com.internship.service;

import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import com.internship.service.exceptoin.NotFoundException;

import java.util.List;

public interface DocumentTypeService {
    List<DocumentTypeDto> getAllDocumentTypes();

    List<DocumentTypeDto> getPageOfTypes(int pageNumber, int pageSize);

    DocumentTypeDto getTypeById(Long id) throws NotFoundException;

    DocumentTypeDto addType(CreateDocumentTypeDto dto);

    DocumentTypeDto updateType(UpdateDocumentTypeDto dto) throws NotFoundException;

    void deleteType(Long id);
}
