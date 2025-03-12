package com.internship.ui.web.controller;

import com.internship.service.DocumentGroupService;
import com.internship.service.dto.group.DocumentGroupDto;
import com.internship.ui.web.dto.group.CreateDocumentGroupRequest;
import com.internship.ui.web.dto.group.UpdateDocumentGroupRequest;
import com.internship.ui.web.mapper.DocumentGroupWebMapper;
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
class DocumentGroupControllerTest {
    private static final long ID = 1L;
    private static final String NAME = "name";
    private static final String COLOR = "color";

    @Mock
    private DocumentGroupService documentGroupService;

    @Mock
    private DocumentGroupWebMapper mapper;

    @InjectMocks
    private DocumentGroupController documentGroupController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(documentGroupController)
                .setValidator(new LocalValidatorFactoryBean())
                .build();
    }

    @Test
    void getAllGroups_shouldReturnPageOfDocumentGroups_whenExist() throws Exception {
        int pageNumber = 0;
        int pageSize = 50;
        boolean hasDocumentsToRenew = true;
        List<DocumentGroupDto> documentGroups = List.of(mock(DocumentGroupDto.class));
        when(documentGroupService.getPageOfGroups(pageNumber, pageSize)).thenReturn(documentGroups);
        mockMvc.perform(get("/group/getAll")
                        .queryParam("pageNumber", String.valueOf(pageNumber))
                        .queryParam("pageSize", String.valueOf(pageSize))
                        .sessionAttr("hasDocumentsToRenew", hasDocumentsToRenew))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attribute("groups", documentGroups))
                .andExpect(model().attributeExists("pageNumber"))
                .andExpect(model().attribute("pageNumber", pageNumber))
                .andExpect(model().attributeExists("hasDocumentsToRenew"))
                .andExpect(model().attribute("hasDocumentsToRenew", hasDocumentsToRenew));
    }

    @Test
    void getGroup_shouldReturnGroupById_whenExist() throws Exception {
        boolean hasDocumentsToRenew = true;
        DocumentGroupDto dto = mock(DocumentGroupDto.class);
        when(documentGroupService.getGroupById(ID)).thenReturn(dto);
        mockMvc.perform(get("/group/get/{id}", ID)
                        .sessionAttr("hasDocumentsToRenew", hasDocumentsToRenew))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", dto))
                .andExpect(model().attributeExists("hasDocumentsToRenew"))
                .andExpect(model().attribute("hasDocumentsToRenew", hasDocumentsToRenew));
    }

    @Test
    void createGroup_shouldReturnGroup_whenSuccessfullyCreated() throws Exception {
        CreateDocumentGroupRequest request = new CreateDocumentGroupRequest(NAME, COLOR);
        DocumentGroupDto dto = new DocumentGroupDto(ID, NAME, COLOR);
        when(documentGroupService.addGroup(mapper.toDto(request))).thenReturn(dto);
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                request.name(), request.color())))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", dto));
    }

    @Test
    void createGroup_shouldReturnBadRequest_whenNameIsNull() throws Exception {
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": %s,
                                            "color": "%s"
                                        }
                                        """,
                                null, COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGroup_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                "", COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGroup_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                " ", COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGroup_shouldReturnBadRequest_whenColorIsNull() throws Exception {
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "color": %s
                                        }
                                        """,
                                NAME, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGroup_shouldReturnBadRequest_whenColorIsEmpty() throws Exception {
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                NAME, "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGroup_shouldReturnBadRequest_whenColorIsBlank() throws Exception {
        mockMvc.perform(post("/group/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                NAME, " ")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnGroup_whenSuccessfullyUpdated() throws Exception {
        UpdateDocumentGroupRequest request = new UpdateDocumentGroupRequest(ID, NAME, COLOR);
        DocumentGroupDto dto = new DocumentGroupDto(ID, NAME, COLOR);
        when(documentGroupService.updateGroup(mapper.toDto(request))).thenReturn(dto);
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                request.id(), request.name(), request.color())))
                .andExpect(status().isOk())
                .andExpect(view().name("group"))
                .andExpect(model().attributeExists("group"))
                .andExpect(model().attribute("group", dto));
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenIdIsNull() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %s,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                null, NAME, COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenIdIsZero() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                0, NAME, COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenIdIsNegative() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                -1, NAME, COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenNameIsNull() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": %s,
                                            "color": "%s"
                                        }
                                        """,
                                ID, null, COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenNameIsEmpty() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                ID, "", COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenNameIsBlank() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                ID, " ", COLOR)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenColorIsNull() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": %s
                                        }
                                        """,
                                ID, NAME, null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenColorIsEmpty() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                ID, NAME, "")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGroup_shouldReturnBadRequest_whenColorIsBlank() throws Exception {
        mockMvc.perform(put("/group/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                        {
                                            "id": %d,
                                            "name": "%s",
                                            "color": "%s"
                                        }
                                        """,
                                ID, NAME, " ")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteGroup_shouldReturnNothing_whenExist() throws Exception {
        mockMvc.perform(delete("/group/delete/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(view().name("groups"));
    }
}