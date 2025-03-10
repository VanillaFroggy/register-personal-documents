package com.internship.service.impl;

import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.User;
import com.internship.persistence.repo.DocumentGroupRepository;
import com.internship.persistence.repo.UserRepository;
import com.internship.service.DocumentGroupService;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.service.mapper.ServiceMapper;
import com.internship.service.utils.Utils;
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
    @Transactional(readOnly = true)
    public List<DocumentGroupDto> getAllDocumentGroups() {
        return documentGroupRepository.findAllByUserId(Utils.getCurrentUserId())
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentGroupDto> getPageOfGroups(int pageNumber, int pageSize) {
        return documentGroupRepository.findAllByUserId(Utils.getCurrentUserId(), PageRequest.of(pageNumber, pageSize))
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentGroupDto getGroupById(Long id) throws AccessException, NotFoundException {
        DocumentGroup documentGroup = documentGroupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        doesUserOwnDocumentGroup(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @Override
    public DocumentGroupDto addGroup(CreateDocumentGroupDto dto) throws NotFoundException {
        DocumentGroup documentGroup = mapper.toEntity(dto);
        documentGroup.setUser(userRepository.findById(Utils.getCurrentUserId())
                .orElseThrow(NotFoundException::new));
        documentGroupRepository.save(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @Override
    public DocumentGroupDto updateGroup(UpdateDocumentGroupDto dto) throws AccessException, NotFoundException {
        DocumentGroup documentGroup = documentGroupRepository.findById(dto.id())
                .orElseThrow(NotFoundException::new);
        doesUserOwnDocumentGroup(documentGroup);
        User user = documentGroup.getUser();
        documentGroup = mapper.toEntity(dto);
        documentGroup.setUser(user);
        documentGroupRepository.save(documentGroup);
        return mapper.toDto(documentGroup);
    }

    @Override
    public void deleteGroup(Long id) throws AccessException, NotFoundException {
        DocumentGroup documentGroup = documentGroupRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        doesUserOwnDocumentGroup(documentGroup);
        documentGroupRepository.delete(documentGroup);
    }

    private static void doesUserOwnDocumentGroup(DocumentGroup documentGroup) throws AccessException {
        if (!documentGroup.getUser().getId().equals(Utils.getCurrentUserId())) {
            throw new AccessException("This is not your document group");
        }
    }
}
