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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
    public List<DocumentDto> getPageOfDocumentsByGroup(Long userId, Long groupId, int pageNumber, int pageSize) {
        return documentRepository.findAllByUserIdAndDocumentGroupId(
                        userId,
                        groupId,
                        PageRequest.of(pageNumber, pageSize))
                .map(mapper::toDto)
                .toList();
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
