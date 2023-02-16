package com.alterra.pos.service;

import com.alterra.pos.entity.Admin;
import com.alterra.pos.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private AdminRepository adminRepository;

    public List<Admin> getAdmins() {
        return adminRepository.findAllByIsValidTrue();
    }

    public Optional<Admin> getAdminById(Integer id) {
        return adminRepository.findById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }
}
