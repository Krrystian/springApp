package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.AdminDTO;
import com.dziekanat.springApp.model.Admin;
import com.dziekanat.springApp.model.Role;
import com.dziekanat.springApp.model.User;
import com.dziekanat.springApp.repository.AdminRepository;
import com.dziekanat.springApp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    // Logger for debugging
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(AdminRepository adminRepository, UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> getAllAdmins() {
        List<Admin> admins = adminRepository.findAll();
        logger.info("Fetched all admins: {}", admins);

        List<AdminDTO> adminDTOs = admins.stream().map(admin -> {
            return new AdminDTO(
                    admin.getUser().getId(),
                    admin.getUser().getFirstName(),
                    admin.getUser().getLastName(),
                    admin.getUser().getUsername(),
                    admin.getPosition()
            );
        }).toList();

        return ResponseEntity.ok(adminDTOs);
    }


    @PostMapping()
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody Admin admin, @RequestParam Integer userId) {
        logger.debug("Creating admin with userId: {}", userId);
        Optional<User> userOptional = userRepository.findById(userId);
        logger.info("User optional: {}", userOptional);

        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();
            logger.info("Existing user: {}", existingUser.getFirstName());

            if (!existingUser.getRole().equals(Role.ADMIN)) {
                logger.warn("User with ID {} is not an admin", userId);
                return ResponseEntity.badRequest().body(null);
            }
            admin.setUser(existingUser);

            Admin savedAdmin = adminRepository.save(admin);
            logger.info("Admin created successfully: {}", savedAdmin);

            AdminDTO adminDTO = new AdminDTO(
                    savedAdmin.getUser().getId(),
                    savedAdmin.getUser().getFirstName(),
                    savedAdmin.getUser().getLastName(),
                    savedAdmin.getUser().getUsername(),
                    savedAdmin.getPosition()
            );
            return ResponseEntity.ok(adminDTO);
        } else {
            logger.error("User with ID {} not found", userId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> getAdminById(@PathVariable Integer id) {
        logger.debug("Fetching admin by ID: {}", id);
        return adminRepository.findById(id)
                .map(admin -> {
                    AdminDTO adminDTO = new AdminDTO(
                            admin.getUser().getId(),
                            admin.getUser().getFirstName(),
                            admin.getUser().getLastName(),
                            admin.getUser().getUsername(),
                            admin.getPosition()
                    );
                    logger.info("Fetched admin: {}", adminDTO);
                    return ResponseEntity.ok(adminDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAdmin(@PathVariable Integer id, @RequestBody Admin adminDetails) {
        logger.debug("Updating admin with ID: {}", id);
        return adminRepository.findById(id)
                .map(admin -> {
                    admin.setPosition(adminDetails.getPosition());
                    Admin updatedAdmin = adminRepository.save(admin);
                    logger.info("Admin updated successfully: {}", updatedAdmin);
                    return ResponseEntity.ok(updatedAdmin.getPosition());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAdmin(@PathVariable Integer id) {
        logger.debug("Deleting admin with ID: {}", id);
        return adminRepository.findById(id)
                .map(admin -> {
                    adminRepository.delete(admin);
                    logger.info("Admin deleted successfully with ID: {}", id);
                    return ResponseEntity.noContent().build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
