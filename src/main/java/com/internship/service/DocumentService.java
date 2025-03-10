package com.internship.service;

import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;

import java.util.List;

public interface DocumentService {
    boolean hasDocumentsToRenew();

    List<DocumentDto> getPageOfDocumentsByGroup(Long groupId, int pageNumber, int pageSize);

    List<DocumentDto> getAllDocumentsToRenew();

    DocumentDto getDocumentById(Long id) throws AccessException, NotFoundException;

    DocumentDto addDocument(CreateDocumentDto dto) throws NotFoundException;

    DocumentDto updateDocument(UpdateDocumentDto dto) throws AccessException, NotFoundException;

    void deleteDocument(Long id) throws AccessException, NotFoundException;
}
