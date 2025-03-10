package com.internship.ui.web.controller;

import com.internship.service.DocumentGroupService;
import com.internship.service.DocumentService;
import com.internship.service.DocumentTypeService;
import com.internship.service.exceptoin.AccessException;
import com.internship.service.exceptoin.NotFoundException;
import com.internship.ui.web.dto.document.CreateDocumentRequest;
import com.internship.ui.web.dto.document.UpdateDocumentRequest;
import com.internship.ui.web.mapper.DocumentWebMapper;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
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
            @RequestParam("groupId") @NotNull @Positive Long groupId,
            @RequestParam("pageNumber") @PositiveOrZero int pageNumber,
            @RequestParam("pageSize") @Min(1) @Max(50) int pageSize,
            @SessionAttribute("hasDocumentsToRenew") Boolean hasDocumentsToRenew
    ) {
        model.addAttribute(
                "documents",
                documentService.getPageOfDocumentsByGroup(groupId, pageNumber, pageSize)
        );
        model.addAttribute("groups", documentGroupService.getAllDocumentGroups());
        model.addAttribute("types", documentTypeService.getAllDocumentTypes());
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("hasDocumentsToRenew", hasDocumentsToRenew);
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
    public String getDocument(
            Model model,
            @PathVariable("id") @NotNull @Positive Long id,
            @SessionAttribute("hasDocumentsToRenew") Boolean hasDocumentsToRenew
    ) throws AccessException, NotFoundException {
        model.addAttribute("document", documentService.getDocumentById(id));
        model.addAttribute("groups", documentGroupService.getAllDocumentGroups());
        model.addAttribute("types", documentTypeService.getAllDocumentTypes());
        model.addAttribute("hasDocumentsToRenew", hasDocumentsToRenew);
        return "document";
    }

    @PostMapping("/create")
    public String createDocument(Model model, @RequestBody @Valid CreateDocumentRequest request)
            throws NotFoundException {
        model.addAttribute("document", documentService.addDocument(mapper.toDto(request)));
        return "documents";
    }

    @PutMapping("/update")
    public String updateDocument(
            Model model,
            HttpSession session,
            @RequestBody @Valid UpdateDocumentRequest request
    ) throws AccessException, NotFoundException {
        model.addAttribute("document", documentService.updateDocument(mapper.toDto(request)));
        session.setAttribute("hasDocumentsToRenew", documentService.hasDocumentsToRenew());
        return "document";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDocument(HttpSession session, @PathVariable("id") @NotNull @Positive Long id)
            throws AccessException, NotFoundException {
        documentService.deleteDocument(id);
        session.setAttribute("hasDocumentsToRenew", documentService.hasDocumentsToRenew());
        return "documents";
    }
}
