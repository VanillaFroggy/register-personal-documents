package com.internship.service.impl;

import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.DocumentGroupService;
import com.internship.service.utils.Utils;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentGroupServiceImpl implements DocumentGroupService {
    private final DocumentGroupRepository documentGroupRepository;
    private final UserRepository userRepository;
    private final ServiceMapper mapper;

    @Override
    public List<DocumentGroupDto> getAllDocumentGroups() {
        return documentGroupRepository.findAllByUserId(Utils.getCurrentUserId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public List<DocumentGroupDto> getPageOfGroups(int pageNumber, int pageSize) {
        return documentGroupRepository.findAllByUserId(Utils.getCurrentUserId(), PageRequest.of(pageNumber, pageSize))
                .map(mapper::toDto)
                .toList();
    }

    @SneakyThrows
    @Override
    public DocumentGroupDto getGroupById(Long id) {
        DocumentGroup documentGroup = documentGroupRepository.findById(id)
                .orElseThrow(NullPointerException::new);
        doesUserOwnDocumentGroup(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @Override
    public DocumentGroupDto addGroup(CreateDocumentGroupDto dto) {
        DocumentGroup documentGroup = DocumentGroup.builder()
                .name(dto.name())
                .color(dto.color())
                .user(userRepository.findById(Utils.getCurrentUserId())
                        .orElseThrow(NullPointerException::new))
                .build();
        documentGroupRepository.save(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @SneakyThrows
    @Override
    public DocumentGroupDto updateGroup(UpdateDocumentGroupDto dto) {
        DocumentGroup documentGroup = documentGroupRepository.findById(dto.id())
                .orElseThrow(NullPointerException::new);
        doesUserOwnDocumentGroup(documentGroup);
        documentGroup = mapper.toEntity(dto);
        documentGroup.setUser(
                userRepository.findById(Utils.getCurrentUserId())
                        .orElseThrow(NullPointerException::new)
        );
        documentGroupRepository.save(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @SneakyThrows
    @Override
    public void deleteGroup(Long id) {
        DocumentGroup documentGroup = documentGroupRepository.findById(id)
                .orElseThrow(NullPointerException::new);
        doesUserOwnDocumentGroup(documentGroup);
        documentGroupRepository.delete(documentGroup);
    }

    private static void doesUserOwnDocumentGroup(DocumentGroup documentGroup) throws IllegalAccessException {
        if (!documentGroup.getUser().getId().equals(Utils.getCurrentUserId())) {
            throw new IllegalAccessException("This is not your document group");
        }
    }
}
