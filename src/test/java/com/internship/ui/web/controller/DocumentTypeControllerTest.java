package com.internship.ui.web.controller;

import com.internship.service.DocumentTypeService;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.ui.web.dto.type.CreateDocumentTypeRequest;
import com.internship.ui.web.dto.type.UpdateDocumentTypeRequest;
import com.internship.ui.web.mapper.DocumentTypeWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DocumentTypeControllerTest {
    private static final long ID = 1L;
    private static final String NAME = "name";
    private static final int DAYS_BEFORE_EXPIRATION_TO_WARN_USER = 7;

    @Mock
    private DocumentTypeService documentTypeService;

    @Mock
    private DocumentTypeWebMapper mapper;

    @InjectMocks
    private DocumentTypeController documentTypeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentTypeController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void getAllTypes_shouldReturnPageOfDocumentTypes_whenExist() throws Exception {
        int pageNumber = 0;
        int pageSize = 50;
        boolean hasDocumentsToRenew = true;
        List<DocumentTypeDto> documentTypes = List.of(mock(DocumentTypeDto.class));
        when(documentTypeService.getPageOfTypes(pageNumber, pageSize)).thenReturn(documentTypes);
        mockMvc.perform(get("/type/getAll")
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .sessionAttr("hasDocumentsToRenew", hasDocumentsToRenew))
                .andExpect(status().isOk())
                .andExpect(view().name("types"))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attribute("types", documentTypes))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attribute("pageNumber", pageNumber))
                .andExpect(model().attributeExists("hasDocumentsToRenew"))
                .andExpect(model().attribute("hasDocumentsToRenew", hasDocumentsToRenew));
    }

    @Test
    void getType_shouldReturnTypeById_whenExist() throws Exception {
        boolean hasDocumentsToRenew = true;
        DocumentTypeDto dto = mock(DocumentTypeDto.class);
        when(documentTypeService.getTypeById(ID)).thenReturn(dto);
        mockMvc.perform(get("/type/get/{id}", ID)
                        .sessionAttr("hasDocumentsToRenew", hasDocumentsToRenew))
                .andExpect(status().isOk())
                .andExpect(view().name("type"))
                .andExpect(model().attributeExists("type"))
                .andExpect(model().attribute("type", dto))
                .andExpect(model().attributeExists("hasDocumentsToRenew"))
                .andExpect(model().attribute("hasDocumentsToRenew", hasDocumentsToRenew));
    }

    @Test
    void createType_shouldReturnType_whenSuccessfullyCreated() throws Exception {
        CreateDocumentTypeRequest request = new CreateDocumentTypeRequest(NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
        DocumentTypeDto dto = new DocumentTypeDto(ID, NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
        when(documentTypeService.addType(mapper.toDto(request))).thenReturn(dto);
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                request.name(), request.daysBeforeExpirationToWarnUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("types"))
                .andExpect(model().attributeExists("type"))
                .andExpect(model().attribute("type", dto));
    }

    @Test
    void createType_shouldReturnBadRequest_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": %s,
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                null, DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createType_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                "", DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createType_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                " ", DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createType_shouldReturnBadRequest_whenDaysBeforeExpirationToWarnUserIsNull() throws Exception {
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %s
                                        }
                                        """,
                                NAME, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createType_shouldReturnBadRequest_whenDaysBeforeExpirationToWarnUserIsZero() throws Exception {
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                NAME, 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createType_shouldReturnBadRequest_whenDaysBeforeExpirationToWarnUserIsNegative() throws Exception {
        mockMvc.perform(post("/type/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                NAME, -1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnType_whenSuccessfullyUpdated() throws Exception {
        UpdateDocumentTypeRequest request = new UpdateDocumentTypeRequest(ID, NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
        DocumentTypeDto dto = new DocumentTypeDto(ID, NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER);
        when(documentTypeService.updateType(mapper.toDto(request))).thenReturn(dto);
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                request.id(), request.name(), request.daysBeforeExpirationToWarnUser())))
                .andExpect(status().isOk())
                .andExpect(view().name("type"))
                .andExpect(model().attributeExists("type"))
                .andExpect(model().attribute("type", dto));
    }

    @Test
    void updateType_shouldReturnBadRequest_whenIdIsNull() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %s,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                null, NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenIdIsZero() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                0, NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenIdIsNegative() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                -1, NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenNameIsNull() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": %s,
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                ID, null, DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                ID, "", DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                ID, " ", DAYS_BEFORE_EXPIRATION_TO_WARN_USER)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenDaysBeforeExpirationToWarnUserIsNull() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %s
                                        }
                                        """,
                                ID, NAME, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenDaysBeforeExpirationToWarnUserIsZero() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                ID, NAME, 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateType_shouldReturnBadRequest_whenDaysBeforeExpirationToWarnUserIsNegative() throws Exception {
        mockMvc.perform(put("/type/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "daysBeforeExpirationToWarnUser": %d
                                        }
                                        """,
                                ID, NAME, -1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteType_shouldReturnNothing_whenExist() throws Exception {
        mockMvc.perform(delete("/type/delete/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(view().name("types"));
    }
}