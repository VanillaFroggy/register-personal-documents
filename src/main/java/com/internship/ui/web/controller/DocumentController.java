package com.internship.ui.web.controller;

import com.internship.service.DocumentService;
import com.internship.ui.web.dto.document.UpdateDocumentRequest;
import com.internship.ui.web.dto.document.CreateDocumentRequest;
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
    private final DocumentWebMapper mapper;

    @GetMapping("/getAll")
    public String document(
            Model model,
            @RequestParam("userId") Long userId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize
    ) {
        model.addAllAttributes(documentService.getPageOfDocuments(userId, pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        return "document";
    }

    @GetMapping("/getAllInGroup")
    public String document(
            Model model,
            @RequestParam("userId") Long userId,
            @RequestParam("groupId") Long groupId,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize
    ) {
        model.addAllAttributes(documentService.getPageOfDocumentsByGroup(userId, groupId, pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        return "document";
    }

    @PostMapping("/create")
    public String document(Model model, @RequestBody CreateDocumentRequest request) {
        model.addAttribute("document", documentService.addDocument(mapper.toDto(request)));
        return "document";
    }

    @PutMapping("/update")
    public String document(Model model, @RequestBody UpdateDocumentRequest request) {
        model.addAttribute("document", documentService.updateDocument(mapper.toDto(request)));
        return "document";
    }

    @DeleteMapping("/delete/{id}")
    public String document(@PathVariable Long id) {
        documentService.deleteDocument(id);
        return "redirect:/";
    }
}
