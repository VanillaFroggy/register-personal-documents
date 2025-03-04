package com.internship.service;

import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentGroupService {

    List<DocumentGroupDto> getAllDocumentGroups();

    List<DocumentGroupDto> getPageOfGroups(@Min(0) int pageNumber, @Min(1) @Max(50) int pageSize);

    DocumentGroupDto getGroupById(@NotNull Long id) throws AccessException, NotFoundException;

    DocumentGroupDto addGroup(@Valid CreateDocumentGroupDto dto) throws NotFoundException;

    DocumentGroupDto updateGroup(@Valid UpdateDocumentGroupDto dto) throws AccessException, NotFoundException;

    void deleteGroup(@NotNull Long id) throws AccessException, NotFoundException;

}
