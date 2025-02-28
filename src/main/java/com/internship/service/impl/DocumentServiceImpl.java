package com.internship.service.impl;

import com.internship.persistence.entity.Document;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.DocumentRepository;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.DocumentService;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentGroupRepository documentGroupRepository;
    private final DocumentTypeRepository documentTypeRepository;
    private final UserRepository userRepository;
    private final ServiceMapper mapper;

    @Override
    public boolean hasDocumentsToRenew(Long userId) {
        boolean hasDocumentsToRenew = false;
        int pageNumber = 0;
        Page<Document> documents = documentRepository.findAllByUserId(
                userId,
                PageRequest.of(pageNumber, 100)
        );
        do {
            if (documents.stream().anyMatch(document ->
                    ChronoUnit.DAYS.between(document.getDateOfIssue(), document.getExpirationDate())
                            <= document.getDocumentType().getDaysBeforeExpirationToWarnUser())
            ) {
                hasDocumentsToRenew = true;
                break;
            }
            documents = documentRepository.findAllByUserId(userId, PageRequest.of(++pageNumber, 100));
        } while (documents.hasContent() && !documents.isLast());
        return hasDocumentsToRenew;
    }

    @Override
    public List<DocumentDto> getPageOfDocumentsByGroup(Long userId, Long groupId, int pageNumber, int pageSize) {
        return documentRepository.findAllByUserIdAndDocumentGroupId(
                        userId,
                        groupId,
                        PageRequest.of(pageNumber, pageSize))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<DocumentDto> getAllDocumentsToRenew(Long userId) {
        int pageNumber = 0;
        Page<Document> documents = documentRepository.findAllByUserId(
                userId,
                PageRequest.of(pageNumber, 1_000)
        );
        List<DocumentDto> documentsToRenew = new ArrayList<>();
        do {
            documentsToRenew.addAll(
                    documents.stream()
                            .filter(document ->
                                    ChronoUnit.DAYS.between(document.getDateOfIssue(), document.getExpirationDate())
                                            <= document.getDocumentType().getDaysBeforeExpirationToWarnUser())
                            .map(mapper::toDto)
                            .toList()
            );
            documents = documentRepository.findAllByUserId(userId, PageRequest.of(++pageNumber, 1_000));
        } while (documents.hasContent() && !documents.isLast() && documentsToRenew.isEmpty());
        return documentsToRenew;
    }

    @Override
    public DocumentDto getDocumentById(Long id) {
        return documentRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NullPointerException::new);
    }

    @Override
    public DocumentDto addDocument(CreateDocumentDto dto) {
        Document document = Document.builder()
                .title(dto.title())
                .documentGroup(documentGroupRepository.findById(dto.documentGroupId())
                        .orElseThrow(NullPointerException::new))
                .documentType(documentTypeRepository.findById(dto.documentTypeId())
                        .orElseThrow(NullPointerException::new))
                .user(userRepository.findById(dto.userId())
                        .orElseThrow(NullPointerException::new))
                .dateOfIssue(ZonedDateTime.now(ZoneOffset.UTC))
                .expirationDate(dto.expirationDate())
                .build();
        documentRepository.save(document);
        return mapper.toDto(document);
    }

    @Override
    public DocumentDto updateDocument(UpdateDocumentDto dto) {
        Document document = mapper.toEntity(dto);
        document.setDocumentType(
                documentTypeRepository.findById(dto.documentTypeId())
                        .orElseThrow(NullPointerException::new)
        );
        document.setDocumentGroup(
                documentGroupRepository.findById(dto.documentGroupId())
                        .orElseThrow(NullPointerException::new)
        );
        document.setUser(
                userRepository.findById(dto.userId())
                        .orElseThrow(NullPointerException::new)
        );
        documentRepository.save(document);
        return mapper.toDto(document);
    }

    @Override
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
}
