package com.internship.service;

import com.internship.service.dto.type.CreateDocumentTypeDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.service.dto.type.UpdateDocumentTypeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface DocumentTypeService {
    List<DocumentTypeDto> getPageOfTypes(
            @Min(0) int pageNumber,
            @Min(1) @Max(50) int pageSize
    );

    DocumentTypeDto addType(@Valid CreateDocumentTypeDto dto);

    DocumentTypeDto updateType(UpdateDocumentTypeDto dto);

    void deleteType(Long id);
}
