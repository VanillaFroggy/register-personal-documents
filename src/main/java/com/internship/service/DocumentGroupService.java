package com.internship.service;

import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;

import java.util.List;

public interface DocumentGroupService {

    List<DocumentGroupDto> getAllDocumentGroups();

    List<DocumentGroupDto> getPageOfGroups(int pageNumber, int pageSize);

    DocumentGroupDto getGroupById(Long id) throws AccessException, NotFoundException;

    DocumentGroupDto addGroup(CreateDocumentGroupDto dto) throws NotFoundException;

    DocumentGroupDto updateGroup(UpdateDocumentGroupDto dto) throws AccessException, NotFoundException;

    void deleteGroup(Long id) throws AccessException, NotFoundException;

}
