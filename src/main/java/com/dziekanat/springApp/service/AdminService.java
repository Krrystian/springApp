package com.dziekanat.springApp.service;

import com.dziekanat.springApp.model.Admin;
import com.dziekanat.springApp.repository.AdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public Admin getAuthenticatedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return adminRepository.findByUserUsername(username)
                .orElseThrow(() -> {
                    return new RuntimeException("Admin not found");
                });
    }
}
