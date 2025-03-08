package com.internship.ui.web.controller;

import com.internship.service.DocumentTypeService;
import com.internship.service.exceptoin.NotFoundException;
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
            @RequestParam("pageSize") int pageSize,
            @SessionAttribute("hasDocumentsToRenew") Boolean hasDocumentsToRenew
    ) {
        model.addAttribute("types", documentTypeService.getPageOfTypes(pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("hasDocumentsToRenew", hasDocumentsToRenew);
        return "types";
    }

    @GetMapping("/get/{id}")
    public String getType(
            Model model,
            @PathVariable("id") Long id,
            @SessionAttribute("hasDocumentsToRenew") Boolean hasDocumentsToRenew
    ) throws NotFoundException {
        model.addAttribute("type", documentTypeService.getTypeById(id));
        model.addAttribute("hasDocumentsToRenew", hasDocumentsToRenew);
        return "type";
    }

    @PostMapping("/create")
    public String createType(Model model, @RequestBody CreateDocumentTypeRequest request) {
        model.addAttribute("type", documentTypeService.addType(mapper.toDto(request)));
        return "types";
    }

    @PutMapping("/update")
    public String updateType(Model model, @RequestBody UpdateDocumentTypeRequest request) throws NotFoundException {
        model.addAttribute("type", documentTypeService.updateType(mapper.toDto(request)));
        return "type";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteType(@PathVariable("id") Long id) {
        documentTypeService.deleteType(id);
        return "types";
    }
}
