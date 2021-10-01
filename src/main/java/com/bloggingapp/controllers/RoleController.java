package com.bloggingapp.controllers;


import com.bloggingapp.entities.RoleEntity;
import com.bloggingapp.exceptions.ApplicationException;
import com.bloggingapp.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService service){
        this.roleService = service;
    }


    @PostMapping("/admin/create/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleEntity> createRole(@RequestBody RoleEntity role){
        if (role.getRoleName().isEmpty() || role.getRoleName() == ""){
            throw new ApplicationException("Role Name Can't Be Empty!");
        }
        return new ResponseEntity<>(roleService.save(role), HttpStatus.CREATED);
    }
}
