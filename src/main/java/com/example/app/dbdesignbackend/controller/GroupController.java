package com.example.app.dbdesignbackend.controller;

import com.example.app.dbdesignbackend.dto.CreateGroupDTO;
import com.example.app.dbdesignbackend.dto.GroupDTO;
import com.example.app.dbdesignbackend.security.User;
import com.example.app.dbdesignbackend.service.GroupService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/groups")
@AllArgsConstructor
public class GroupController {

    private GroupService groupService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<GroupDTO>> findAll() {
        List<GroupDTO> groups = groupService.findAll();
        return ResponseEntity.ok(groups);
    }

    @GetMapping("/teacher/my")
    @PreAuthorize("hasAuthority('TEACHER')")
    public ResponseEntity<List<GroupDTO>> findAllForTeacher(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();
        List<GroupDTO> groups = groupService.findAllForTeacher(user.getId());
        return ResponseEntity.ok(groups);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> createGroup(
            @RequestBody CreateGroupDTO createGroupDTO
    ) {
        groupService.createGroup(createGroupDTO);
        return ResponseEntity.ok().build();
    }

}
