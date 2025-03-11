package com.internship.service.mapper;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.entity.User;
import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.dto.document.CreateDocumentDto;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.dto.group.CreateDocumentGroupDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import org.mapstruct.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {ZonedDateTime.class, ZoneOffset.class})
public interface ServiceMapper {
    @Named("mapZonedDateTime")
    default ZonedDateTime mapZonedDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }

    @Mapping(target = "dateOfIssue", expression = "java(ZonedDateTime.now(ZoneOffset.UTC))")
    @Mapping(target = "expirationDate", source = "expirationDate", qualifiedByName = "mapZonedDateTime")
    Document toEntity(CreateDocumentDto createDocumentDto);

    @Mapping(target = "dateOfIssue", source = "dateOfIssue", qualifiedByName = "mapZonedDateTime")
    @Mapping(target = "expirationDate", source = "expirationDate", qualifiedByName = "mapZonedDateTime")
    Document toEntity(UpdateDocumentDto dto);

    DocumentDto toDto(Document entity);

    DocumentGroupDto toDto(DocumentGroup entity);

    DocumentGroup toEntity(CreateDocumentGroupDto dto);

    DocumentGroup toEntity(UpdateDocumentGroupDto dto);

    DocumentType toEntity(CreateDocumentTypeDto dto);

    DocumentTypeDto toDto(DocumentType entity);

    DocumentType toEntity(UpdateDocumentTypeDto dto);

    User toEntity(RegisterDto dto);
}
