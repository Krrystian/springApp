package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.AnnouncementDTO;
import com.dziekanat.springApp.model.Announcement;
import com.dziekanat.springApp.model.Admin;
import com.dziekanat.springApp.repository.AnnouncementRepository;
import com.dziekanat.springApp.repository.AdminRepository;
import com.dziekanat.springApp.service.AdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementController.class);

    private final AnnouncementRepository announcementRepository;
    private final AdminService adminService;

    public AnnouncementController(AnnouncementRepository announcementRepository, AdminRepository adminRepository, AdminService adminService) {
        this.announcementRepository = announcementRepository;
        this.adminService = adminService;
    }

    // Create announcement
    @PostMapping
    public ResponseEntity<AnnouncementDTO> createAnnouncement(@RequestBody AnnouncementDTO announcementDTO) {
        logger.info("Received request to create announcement with title: {}", announcementDTO.getTitle());

        Admin admin = adminService.getAuthenticatedAdmin();
        logger.debug("Authenticated admin: {}", admin);

        Announcement announcement = new Announcement(announcementDTO.getTitle(), announcementDTO.getContent(), admin);
        announcementRepository.save(announcement);
        logger.info("Announcement created with ID: {}", announcement.getId());

        return ResponseEntity.ok(new AnnouncementDTO(announcement.getId(), announcement.getTitle(), announcement.getContent(), announcement.getAdmin().getFullName()));
    }

    @GetMapping
    public ResponseEntity<List<AnnouncementDTO>> getAllAnnouncements() {
        logger.info("Received request to fetch all announcements");

        List<AnnouncementDTO> announcementDTOs = announcementRepository.findAll().stream()
                .map(announcement -> new AnnouncementDTO(
                        announcement.getId(),
                        announcement.getTitle(),
                        announcement.getContent(),
                        announcement.getAdmin().getFullName()
                ))
                .collect(Collectors.toList());

        logger.info("Fetched {} announcements", announcementDTOs.size());
        return ResponseEntity.ok(announcementDTOs);
    }

    // Get announcement by ID
    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncementById(@PathVariable Integer id) {
        logger.info("Received request to fetch announcement with ID: {}", id);

        return announcementRepository.findById(id)
                .map(announcement -> {
                    Admin admin = announcement.getAdmin();
                    String authorFullName = admin.getFullName();

                    AnnouncementDTO announcementDTO = new AnnouncementDTO(
                            announcement.getId(),
                            announcement.getTitle(),
                            announcement.getContent(),
                            authorFullName
                    );

                    logger.info("Announcement found with ID: {}", id);
                    return ResponseEntity.ok(announcementDTO);
                })
                .orElseGet(() -> {
                    logger.warn("Announcement with ID: {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Update announcement
    @PutMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> updateAnnouncement(@PathVariable Integer id,
                                                           @RequestBody Announcement announcementDetails) {
        logger.info("Received request to update announcement with ID: {}", id);

        return announcementRepository.findById(id)
                .map(announcement -> {
                    logger.debug("Updating announcement with new details: {}", announcementDetails);

                    if (announcementDetails.getTitle() != null) {
                        announcement.setTitle(announcementDetails.getTitle());
                    }
                    if (announcementDetails.getContent() != null) {
                        announcement.setContent(announcementDetails.getContent());
                    }
                    Announcement updatedAnnouncement = announcementRepository.save(announcement);

                    logger.info("Announcement updated with ID: {}", updatedAnnouncement.getId());

                    AnnouncementDTO announcementDTO = new AnnouncementDTO(updatedAnnouncement.getId(), updatedAnnouncement.getTitle(),updatedAnnouncement.getContent(), updatedAnnouncement.getAdmin().getFullName());
                    return ResponseEntity.ok(announcementDTO);
                })
                .orElseGet(() -> {
                    logger.warn("Announcement with ID: {} not found for update", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Delete announcement
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAnnouncement(@PathVariable Integer id) {
        logger.info("Received request to delete announcement with ID: {}", id);

        return announcementRepository.findById(id)
                .map(announcement -> {
                    announcementRepository.delete(announcement);
                    logger.info("Announcement deleted with ID: {}", id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> {
                    logger.warn("Announcement with ID: {} not found for deletion", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
