package com.internship.service;

import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentGroupService {

    List<DocumentGroupDto> getPageOfGroups(
            @NotNull Long userId,
            @Min(0) int pageNumber,
            @Min(1) @Max(50) int pageSize
    );

    DocumentGroupDto addGroup(@Valid CreateDocumentGroupDto dto);

    DocumentGroupDto updateGroup(@Valid UpdateDocumentGroupDto dto);

    void deleteGroup(Long id);
}
