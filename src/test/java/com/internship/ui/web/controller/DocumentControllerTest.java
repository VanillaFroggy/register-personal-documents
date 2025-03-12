package com.internship.ui.web.controller;

import com.internship.service.DocumentGroupService;
import com.internship.service.DocumentService;
import com.internship.service.DocumentTypeService;
import com.internship.service.dto.document.DocumentDto;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.service.dto.type.DocumentTypeDto;
import com.internship.ui.web.dto.document.CreateDocumentRequest;
import com.internship.ui.web.dto.document.UpdateDocumentRequest;
import com.internship.ui.web.mapper.DocumentWebMapper;
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

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {
    private static final long ID = 1L;
    private static final long GROUP_ID = 1L;
    private static final long TYPE_ID = 1L;
    private static final String TITLE = "title";
    private static final ZonedDateTime DATE_OF_ISSUE = ZonedDateTime.now(ZoneOffset.UTC);
    private static final ZonedDateTime EXPIRATION_DATE = DATE_OF_ISSUE.plusDays(1);
    private static final String TYPE_NAME = "typeName";
    private static final int DAYS_BEFORE_EXPIRATION_TO_WARN_USER = 7;
    private static final String GROUP_NAME = "groupName";
    private static final String GROUP_COLOR = "color";


    @Mock
    private DocumentService documentService;

    @Mock
    private DocumentGroupService groupService;

    @Mock
    private DocumentTypeService typeService;

    @Mock
    private DocumentWebMapper mapper;

    @InjectMocks
    private DocumentController documentController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void getAllDocumentsInGroup_shouldReturnPageOfDocuments_whenExist() throws Exception {
        int pageNumber = 0;
        int pageSize = 50;
        boolean hasDocumentsToRenew = true;
        List<DocumentDto> documents = List.of(mock(DocumentDto.class));
        List<DocumentGroupDto> groups = List.of(mock(DocumentGroupDto.class));
        List<DocumentTypeDto> types = List.of(mock(DocumentTypeDto.class));

        when(documentService.getPageOfDocumentsByGroup(GROUP_ID, pageNumber, pageSize)).thenReturn(documents);
        when(groupService.getAllDocumentGroups()).thenReturn(groups);
        when(typeService.getAllDocumentTypes()).thenReturn(types);

        mockMvc.perform(get("/document/getAllInGroup")
                        .queryParam("groupId", String.valueOf(GROUP_ID))
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .sessionAttr("hasDocumentsToRenew", hasDocumentsToRenew))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"))
                .andExpect(model().attributeExists("documents"))
                .andExpect(model().attribute("documents", documents))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attribute("types", types))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attribute("pageNumber", pageNumber))
                .andExpect(model().attributeExists("hasDocumentsToRenew"))
                .andExpect(model().attribute("hasDocumentsToRenew", hasDocumentsToRenew));
    }

    @Test
    void getAllDocumentsToRenew_shouldReturnAllDocumentsToRenew_whenExist() throws Exception {
        List<DocumentDto> documents = List.of(mock(DocumentDto.class));
        List<DocumentGroupDto> groups = List.of(mock(DocumentGroupDto.class));
        List<DocumentTypeDto> types = List.of(mock(DocumentTypeDto.class));

        when(documentService.getAllDocumentsToRenew()).thenReturn(documents);
        when(groupService.getAllDocumentGroups()).thenReturn(groups);
        when(typeService.getAllDocumentTypes()).thenReturn(types);

        mockMvc.perform(get("/document/getAllToRenew"))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"))
                .andExpect(model().attributeExists("documents"))
                .andExpect(model().attribute("documents", documents))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", groups))
                .andExpect(model().attributeExists("types"))
                .andExpect(model().attribute("types", types));
    }

    @Test
    void getDocument_shouldReturnDocumentById_whenExist() throws Exception {
        boolean hasDocumentsToRenew = true;
        DocumentDto dto = mock(DocumentDto.class);
        when(documentService.getDocumentById(ID)).thenReturn(dto);
        mockMvc.perform(get("/document/get/{id}", ID)
                        .sessionAttr("hasDocumentsToRenew", hasDocumentsToRenew))
                .andExpect(status().isOk())
                .andExpect(view().name("document"))
                .andExpect(model().attributeExists("document"))
                .andExpect(model().attribute("document", dto))
                .andExpect(model().attributeExists("hasDocumentsToRenew"))
                .andExpect(model().attribute("hasDocumentsToRenew", hasDocumentsToRenew));
    }

    @Test
    void createDocument_shouldReturnDocument_whenSuccessfullyCreated() throws Exception {
        CreateDocumentRequest request = new CreateDocumentRequest(TITLE, TYPE_ID, GROUP_ID, EXPIRATION_DATE);
        DocumentDto dto = new DocumentDto(
                ID,
                TITLE,
                DATE_OF_ISSUE,
                EXPIRATION_DATE,
                new DocumentTypeDto(TYPE_ID, TYPE_NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER),
                new DocumentGroupDto(GROUP_ID, GROUP_NAME, GROUP_COLOR)
        );

        when(documentService.addDocument(mapper.toDto(request))).thenReturn(dto);

        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"))
                .andExpect(model().attributeExists("document"))
                .andExpect(model().attribute("document", dto));
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": %s,
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                null, TYPE_ID, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenTitleIsEmpty() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                "", TYPE_ID, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenTitleIsBlank() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                " ", TYPE_ID, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenTypeIdIsNull() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %s,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, null, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenTypeIdIsZero() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, 0, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenTypeIdIsNegative() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, -1, GROUP_ID, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenGroupIdIsNull() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %s,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, null, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenGroupIdIsZero() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, 0, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenGroupIdIsNegative() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, -1, EXPIRATION_DATE)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenExpirationDateIsNull() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": %s
                                        }
                                        """,
                                TITLE, TYPE_ID, GROUP_ID, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenExpirationDateIsPresent() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, GROUP_ID, ZonedDateTime.now(ZoneOffset.UTC))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenExpirationDateIsPast() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, GROUP_ID, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createDocument_shouldReturnBadRequest_whenExpirationDateHasWrongFormat() throws Exception {
        mockMvc.perform(post("/document/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "title": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d,
                                            "expirationDate": "%s"
                                        }
                                        """,
                                TITLE, TYPE_ID, GROUP_ID, ZonedDateTime.now(ZoneOffset.UTC)
                                        .format(DateTimeFormatter.RFC_1123_DATE_TIME))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnDocument_whenSuccessfullyUpdated() throws Exception {
        UpdateDocumentRequest request =
                new UpdateDocumentRequest(ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID);
        DocumentDto dto = new DocumentDto(
                ID,
                TITLE,
                DATE_OF_ISSUE,
                EXPIRATION_DATE,
                new DocumentTypeDto(TYPE_ID, TYPE_NAME, DAYS_BEFORE_EXPIRATION_TO_WARN_USER),
                new DocumentGroupDto(GROUP_ID, GROUP_NAME, GROUP_COLOR)
        );

        when(documentService.updateDocument(mapper.toDto(request))).thenReturn(dto);

        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isOk())
                .andExpect(view().name("document"))
                .andExpect(model().attributeExists("document"))
                .andExpect(model().attribute("document", dto));
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenIdIsNull() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %s,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                null, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenIdIsZero() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                0, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenIdIsNegative() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                -1, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenTitleIsNull() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": %s,
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, null, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenTitleIsEmpty() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, "", DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenTitleIsBlank() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, " ", DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenDateOfIssueIsNull() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": %s,
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, null, EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenDateOfIssueIsFuture() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, ZonedDateTime.now(ZoneOffset.UTC).plusDays(1),
                                EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenDateOfIssueHasWrongFormat() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, ZonedDateTime.now(ZoneOffset.UTC)
                                        .format(DateTimeFormatter.RFC_1123_DATE_TIME),
                                EXPIRATION_DATE, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenExpirationDateIsNull() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": %s,
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, null, TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenExpirationDateIsPresent() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, ZonedDateTime.now(ZoneOffset.UTC), TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenExpirationDateIsPast() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, ZonedDateTime.now(ZoneOffset.UTC).minusDays(1),
                                TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenExpirationDateHasWrongFormat() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE.format(DateTimeFormatter.RFC_1123_DATE_TIME),
                                TYPE_ID, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenTypeIdIsNull() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %s,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, null, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenTypeIdIsZero() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, 0, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenTypeIdIsNegative() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, -1, GROUP_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenGroupIdIsNull() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %s
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenGroupIdIsZero() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, 0)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateDocument_shouldReturnBadRequest_whenGroupIdIsNegative() throws Exception {
        mockMvc.perform(put("/document/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "title": "%s",
                                            "dateOfIssue": "%s",
                                            "expirationDate": "%s",
                                            "documentTypeId": %d,
                                            "documentGroupId": %d
                                        }
                                        """,
                                ID, TITLE, DATE_OF_ISSUE, EXPIRATION_DATE, TYPE_ID, -1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteDocument_shouldReturnNothing_whenExist() throws Exception {
        mockMvc.perform(delete("/document/delete/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(view().name("documents"));
    }
}