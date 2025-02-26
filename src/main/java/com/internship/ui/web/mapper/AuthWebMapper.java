package com.internship.ui.web.mapper;

import com.internship.service.dto.auth.RegisterDto;
import com.internship.ui.web.dto.auth.RegisterRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AuthWebMapper {
    RegisterDto toDto(RegisterRequest request);
}
