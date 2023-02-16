package com.alterra.pos.controller;

import com.alterra.pos.entity.Admin;
import com.alterra.pos.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admins")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping
    public List<Admin> getAdmins() {
        return adminService.getAdmins();
    }

    @GetMapping("/{id}")
    public Optional<Admin> getAdminById(@PathVariable Integer id) {
        return adminService.getAdminById(id);
    }

    @PostMapping
    public Admin addAdmin(@Validated @RequestBody Admin admin) {
        return adminService.addAdmin(admin);
    }
}
