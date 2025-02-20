package com.internship.ui.web.mapper;

import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.ui.web.dto.document.CreateDocumentRequest;
import com.internship.ui.web.dto.document.UpdateDocumentRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface DocumentWebMapper {
    CreateDocumentDto toDto(CreateDocumentRequest request);

    UpdateDocumentDto toDto(UpdateDocumentRequest request);
}
