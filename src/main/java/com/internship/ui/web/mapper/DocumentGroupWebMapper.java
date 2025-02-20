package com.internship.ui.web.mapper;

import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.ui.web.dto.group.CreateDocumentGroupRequest;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.ui.web.dto.group.UpdateDocumentGroupRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentGroupWebMapper {
    CreateDocumentGroupDto toDto(CreateDocumentGroupRequest createDocumentGroupRequest);

    UpdateDocumentGroupDto toDto(UpdateDocumentGroupRequest request);
}
