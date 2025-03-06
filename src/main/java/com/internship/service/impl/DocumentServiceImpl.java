package com.internship.service.impl;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.DocumentRepository;
import com.internship.persistence.repo.DocumentTypeRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.DocumentService;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.mapper.ServiceMapper;
import com.internship.service.utils.Utils;
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
    @Transactional(readOnly = true)
    public boolean hasDocumentsToRenew() {
        boolean hasDocumentsToRenew = false;
        int pageNumber = 0;
        Long userId = Utils.getCurrentUserId();
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
    @Transactional(readOnly = true)
    public List<DocumentDto> getPageOfDocumentsByGroup(Long groupId, int pageNumber, int pageSize) {
        return documentRepository.findAllByUserIdAndDocumentGroupId(
                        Utils.getCurrentUserId(),
                        groupId,
                        PageRequest.of(pageNumber, pageSize))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentDto> getAllDocumentsToRenew() {
        int pageNumber = 0;
        Long userId = Utils.getCurrentUserId();
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
    @Transactional(readOnly = true)
    public DocumentDto getDocumentById(Long id) throws AccessException, NotFoundException {
        Document document = documentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        doesUserOwnDocument(document);
        return mapper.toDto(document);
    }

    @Override
    public DocumentDto addDocument(CreateDocumentDto dto) throws NotFoundException {
        Document document = Document.builder()
                .title(dto.title())
                .documentGroup(documentGroupRepository.findById(dto.documentGroupId())
                        .orElseThrow(NotFoundException::new))
                .documentType(documentTypeRepository.findById(dto.documentTypeId())
                        .orElseThrow(NotFoundException::new))
                .user(userRepository.findById(Utils.getCurrentUserId())
                        .orElseThrow(NotFoundException::new))
                .dateOfIssue(ZonedDateTime.now(ZoneOffset.UTC))
                .expirationDate(dto.expirationDate().withZoneSameInstant(ZoneOffset.UTC))
                .build();
        documentRepository.save(document);
        return mapper.toDto(document);
    }

    @Override
    public DocumentDto updateDocument(UpdateDocumentDto dto) throws AccessException, NotFoundException {
        Document document = documentRepository.findById(dto.id())
                .orElseThrow(NotFoundException::new);
        doesUserOwnDocument(document);
        User user = document.getUser();
        document = mapper.toEntity(dto);
        document.setDocumentType(
                documentTypeRepository.findById(dto.documentTypeId())
                        .orElseThrow(NotFoundException::new)
        );
        document.setDocumentGroup(
                documentGroupRepository.findById(dto.documentGroupId())
                        .orElseThrow(NotFoundException::new)
        );
        document.setUser(user);
        documentRepository.save(document);
        return mapper.toDto(document);
    }

    @Override
    public void deleteDocument(Long id) throws AccessException, NotFoundException {
        Document document = documentRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        doesUserOwnDocument(document);
        documentRepository.delete(document);
    }

    private static void doesUserOwnDocument(Document document) throws AccessException {
        if (!document.getUser().getId().equals(Utils.getCurrentUserId())) {
            throw new AccessException("This is not your document");
        }
    }
}
