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

    @Override
    public DocumentGroupDto getGroupById(Long id) {
        return documentGroupRepository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(NullPointerException::new);
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

    @Override
    public DocumentGroupDto updateGroup(UpdateDocumentGroupDto dto) {
        DocumentGroup documentGroup = mapper.toEntity(dto);
        documentGroup.setUser(
                userRepository.findById(Utils.getCurrentUserId())
                        .orElseThrow(NullPointerException::new)
        );
        documentGroupRepository.save(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @Override
    public void deleteGroup(Long id) {
        documentGroupRepository.deleteById(id);
    }
}
