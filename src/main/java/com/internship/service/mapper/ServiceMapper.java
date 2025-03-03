package com.internship.service.mapper;

import com.internship.persistence.entity.Document;
import com.internship.persistence.entity.DocumentGroup;
import com.internship.persistence.entity.DocumentType;
import com.internship.persistence.entity.User;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.document.UpdateDocumentDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.group.UpdateDocumentGroupDto;
import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import com.internship.service.dto.auth.RegisterDto;
import com.internship.service.dto.user.UserDto;
import org.mapstruct.*;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ServiceMapper {
    @Named("mapZonedDateTime")
    default ZonedDateTime mapZonedDateTime(ZonedDateTime zonedDateTime) {
        return zonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
    }

    @Mapping(target = "dateOfIssue", source = "dateOfIssue", qualifiedByName = "mapZonedDateTime")
    @Mapping(target = "expirationDate", source = "expirationDate", qualifiedByName = "mapZonedDateTime")
    Document toEntity(UpdateDocumentDto dto);

    @Named("mapUserId")
    default Long mapUserId(User user) {
        return user.getId();
    }

    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserId")
    DocumentDto toDto(Document entity);

    @Mapping(target = "userId", source = "user", qualifiedByName = "mapUserId")
    DocumentGroupDto toDto(DocumentGroup entity);

    DocumentGroup toEntity(UpdateDocumentGroupDto dto);

    DocumentType toEntity(CreateDocumentTypeDto dto);

    DocumentTypeDto toDto(DocumentType entity);

    DocumentType toEntity(UpdateDocumentTypeDto dto);

    User toEntity(RegisterDto dto);

    UserDto toDto(User user);
}
