package com.internship.ui.web.controller;

import com.internship.service.DocumentTypeService;
import com.internship.ui.web.dto.type.CreateDocumentTypeRequest;
import com.internship.ui.web.dto.type.UpdateDocumentTypeRequest;
import com.internship.ui.web.mapper.DocumentTypeWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/type")
@RequiredArgsConstructor
public class DocumentTypeController {
    private final DocumentTypeService documentTypeService;
    private final DocumentTypeWebMapper mapper;

    @GetMapping("/getAll")
    public String type(Model model, @RequestParam int pageNumber, @RequestParam int pageSize) {
        model.addAllAttributes(documentTypeService.getPageOfTypes(pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        return "type";
    }

    @PostMapping("/create")
    public String type(Model model, @RequestBody CreateDocumentTypeRequest request) {
        model.addAttribute("type", documentTypeService.addType(mapper.toDto(request)));
        return "type";
    }

    @PutMapping("/update")
    public String type(Model model, @RequestBody UpdateDocumentTypeRequest request) {
        model.addAttribute("type", documentTypeService.updateType(mapper.toDto(request)));
        return "type";
    }

    @DeleteMapping("/delete/{id}")
    public String type(@PathVariable Long id) {
        documentTypeService.deleteType(id);
        return "redirect:/";
    }
}
