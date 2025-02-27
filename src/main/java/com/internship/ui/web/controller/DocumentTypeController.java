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
    public String getAllTypes(
            Model model,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize
    ) {
        model.addAttribute("types", documentTypeService.getPageOfTypes(pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        return "types";
    }

    @GetMapping("/get/{id}")
    public String getType(Model model, @PathVariable("id") Long id) {
        model.addAttribute("type", documentTypeService.getTypeById(id));
        return "type";
    }

    @PostMapping("/create")
    public String createType(Model model, @RequestBody CreateDocumentTypeRequest request) {
        model.addAttribute("type", documentTypeService.addType(mapper.toDto(request)));
        return "types";
    }

    @PutMapping("/update")
    public String updateType(Model model, @RequestBody UpdateDocumentTypeRequest request) {
        model.addAttribute("type", documentTypeService.updateType(mapper.toDto(request)));
        return "type";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteType(@PathVariable("id") Long id) {
        documentTypeService.deleteType(id);
        return "types";
    }
}
