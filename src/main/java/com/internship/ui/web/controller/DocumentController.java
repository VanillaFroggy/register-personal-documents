package com.internship.ui.web.controller;

import com.internship.service.DocumentGroupService;
import com.internship.service.DocumentService;
import com.internship.service.DocumentTypeService;
import com.internship.ui.web.dto.document.CreateDocumentRequest;
import com.internship.ui.web.dto.document.UpdateDocumentRequest;
import com.internship.ui.web.mapper.DocumentWebMapper;
import com.internship.ui.web.utils.WebUtils;
import jakarta.servlet.http.HttpServletResponse;
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
            @RequestParam("groupId") Long groupId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize
    ) {
        model.addAttribute(
                "documents",
                documentService.getPageOfDocumentsByGroup(groupId, pageNumber, pageSize)
        );
        model.addAttribute("groups", documentGroupService.getAllDocumentGroups());
        model.addAttribute("types", documentTypeService.getAllDocumentTypes());
        model.addAttribute("pageNumber", pageNumber);
        return "documents";
    }

    @GetMapping("/getAllToRenew")
    public String getAllDocumentsToRenew(Model model) {
        model.addAttribute("documents", documentService.getAllDocumentsToRenew());
        model.addAttribute("groups", documentGroupService.getAllDocumentGroups());
        model.addAttribute("types", documentTypeService.getAllDocumentTypes());
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
    public String updateDocument(
            Model model,
            HttpServletResponse response,
            @RequestBody UpdateDocumentRequest request
    ) {
        model.addAttribute("document", documentService.updateDocument(mapper.toDto(request)));
        WebUtils.addCookieToResponse(
                response,
                "hasDocumentsToRenew",
                String.valueOf(documentService.hasDocumentsToRenew())
        );
        return "document";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDocument(@PathVariable("id") Long id) {
        documentService.deleteDocument(id);
        return "documents";
    }
}
