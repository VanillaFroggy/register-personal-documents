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
    public String getAllGroups(
            Model model,
            @RequestParam("pageNumber") int pageNumber,
            @RequestParam("pageSize") int pageSize,
            @SessionAttribute("hasDocumentsToRenew") Boolean hasDocumentsToRenew
    ) {
        model.addAttribute("groups", documentGroupService.getPageOfGroups(pageNumber, pageSize));
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("hasDocumentsToRenew", hasDocumentsToRenew);
        return "groups";
    }

    @GetMapping("/get/{id}")
    public String getGroup(
            Model model,
            @PathVariable("id") Long id,
            @SessionAttribute("hasDocumentsToRenew") Boolean hasDocumentsToRenew
    ) {
        model.addAttribute("group", documentGroupService.getGroupById(id));
        model.addAttribute("hasDocumentsToRenew", hasDocumentsToRenew);
        return "group";
    }

    @PostMapping("/create")
    public String createGroup(Model model, @RequestBody CreateDocumentGroupRequest request) {
        model.addAttribute("group", documentGroupService.addGroup(mapper.toDto(request)));
        return "groups";
    }

    @PutMapping("/update")
    public String updateGroup(Model model, @RequestBody UpdateDocumentGroupRequest request) {
        model.addAttribute("group", documentGroupService.updateGroup(mapper.toDto(request)));
        return "group";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteGroup(@PathVariable("id") Long id) {
        documentGroupService.deleteGroup(id);
        return "groups";
    }
}
