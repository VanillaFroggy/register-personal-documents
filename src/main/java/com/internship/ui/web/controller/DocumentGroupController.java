package com.internship.ui.web.controller;

import com.internship.service.DocumentGroupService;
import com.internship.ui.web.dto.group.CreateDocumentGroupRequest;
import com.internship.ui.web.dto.group.UpdateDocumentGroupRequest;
import com.internship.ui.web.mapper.DocumentGroupWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/group")
@RequiredArgsConstructor
public class DocumentGroupController {
    private final DocumentGroupService documentGroupService;
    private final DocumentGroupWebMapper mapper;

    @GetMapping("/getAll")
    public String group(
            Model model,
            @RequestParam Long userId,
            @RequestParam int pageNumber,
            @RequestParam int pageSize
    ) {
        model.addAllAttributes(documentGroupService.getPageOfGroups(userId, pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        return "group";
    }

    @PostMapping("/create")
    public String group(Model model, @RequestBody CreateDocumentGroupRequest request) {
        model.addAttribute("group", documentGroupService.addGroup(mapper.toDto(request)));
        return "group";
    }

    @PutMapping("/update")
    public String group(Model model, @RequestBody UpdateDocumentGroupRequest request) {
        model.addAttribute("group", documentGroupService.updateGroup(mapper.toDto(request)));
        return "group";
    }

    @DeleteMapping("/delete/{id}")
    public String group(@PathVariable Long id) {
        documentGroupService.deleteGroup(id);
        return "redirect:/";
    }
}
