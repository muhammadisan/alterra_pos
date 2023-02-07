package com.alterra.pos.controller;

import com.alterra.pos.entity.Admin;
import com.alterra.pos.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

    @GetMapping
    public List<Admin> getAdmins() {
        return adminRepository.findAllByIsValidTrue();
    }

    @GetMapping("/{id}")
    public Optional<Admin> getAdminById(@PathVariable Integer id) { return adminRepository.findById(id); }

    @PostMapping
    public Admin addAdmin(@Validated @RequestBody Admin admin) {
        return adminRepository.save(admin);
    }
}
