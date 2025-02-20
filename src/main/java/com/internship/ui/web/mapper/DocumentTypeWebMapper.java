package com.internship.ui.web.mapper;

import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import com.internship.ui.web.dto.type.CreateDocumentTypeRequest;
import com.internship.ui.web.dto.type.UpdateDocumentTypeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentTypeWebMapper {
    CreateDocumentTypeDto toDto(CreateDocumentTypeRequest request);

    UpdateDocumentTypeDto toDto(UpdateDocumentTypeRequest request);
}
