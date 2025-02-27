package com.internship.ui.web.controller;

import com.internship.service.DocumentGroupService;
import com.internship.service.DocumentService;
import com.internship.service.DocumentTypeService;
import com.internship.ui.web.dto.document.CreateDocumentRequest;
import com.internship.ui.web.dto.document.UpdateDocumentRequest;
import com.internship.ui.web.mapper.DocumentWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;
    private final DocumentGroupService documentGroupService;
    private final DocumentTypeService documentTypeService;
    private final DocumentWebMapper mapper;

    @GetMapping("/getAllInGroup")
    public String getAllDocumentsInGroup(
            Model model,
            @RequestParam("userId") Long userId,
            @RequestParam("groupId") Long groupId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize
    ) {
        model.addAttribute(
                "documents",
                documentService.getPageOfDocumentsByGroup(userId, groupId, pageNumber, pageSize)
        );
        model.addAttribute("userId", userId);
        model.addAttribute("groups", documentGroupService.getAllDocumentGroups());
        model.addAttribute("types", documentTypeService.getAllDocumentTypes());
        model.addAttribute("pageNumber", pageNumber);
        return "documents";
    }

    @GetMapping("/get/{id}")
    public String getDocument(Model model, @PathVariable("id") Long id) {
        model.addAttribute("document", documentService.getDocumentById(id));
        model.addAttribute("groups", documentGroupService.getAllDocumentGroups());
        model.addAttribute("types", documentTypeService.getAllDocumentTypes());
        return "document";
    }

    @PostMapping("/create")
    public String createDocument(Model model, @RequestBody CreateDocumentRequest request) {
        model.addAttribute("document", documentService.addDocument(mapper.toDto(request)));
        return "documents";
    }

    @PutMapping("/update")
    public String updateDocument(Model model, @RequestBody UpdateDocumentRequest request) {
        model.addAttribute("document", documentService.updateDocument(mapper.toDto(request)));
        return "document";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDocument(@PathVariable("id") Long id) {
        documentService.deleteDocument(id);
        return "documents";
    }
}
