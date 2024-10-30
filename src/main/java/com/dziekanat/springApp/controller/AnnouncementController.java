package com.dziekanat.springApp.controller;

import com.dziekanat.springApp.dto.AnnouncementDTO;
import com.dziekanat.springApp.model.Announcement;
import com.dziekanat.springApp.model.Admin;
import com.dziekanat.springApp.repository.AnnouncementRepository;
import com.dziekanat.springApp.repository.AdminRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//TODO: SPRAWDZ REQUESTY
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementRepository announcementRepository;
    private final AdminRepository adminRepository;

    public AnnouncementController(AnnouncementRepository announcementRepository, AdminRepository adminRepository) {
        this.announcementRepository = announcementRepository;
        this.adminRepository = adminRepository;
    }

    // Tworzenie ogłoszenia
    @PostMapping
    public ResponseEntity<Announcement> createAnnouncement(@RequestParam String title,
                                                           @RequestParam String content,
                                                           @RequestParam Integer adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Announcement announcement = new Announcement(title, content, admin);
        announcementRepository.save(announcement);
        return ResponseEntity.ok(announcement);
    }

    // Pobranie wszystkich ogłoszeń
    @GetMapping
    public ResponseEntity<List<Announcement>> getAllAnnouncements() {
        List<Announcement> announcements = announcementRepository.findAll();
        return ResponseEntity.ok(announcements);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnouncementDTO> getAnnouncementById(@PathVariable Integer id) {
        return announcementRepository.findById(id)
                .map(announcement -> {
                    Admin admin = announcement.getAdmin(); // Zakładam, że w ogłoszeniu jest pole admin
                    String authorFullName = admin.getFullName(); // Uzyskanie pełnego imienia i nazwiska autora

                    AnnouncementDTO announcementDTO = new AnnouncementDTO(
                            announcement.getId(),
                            announcement.getTitle(),
                            announcement.getContent(),
                            authorFullName
                    );

                    return ResponseEntity.ok(announcementDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    // Aktualizacja ogłoszenia
    @PutMapping("/{id}")
    public ResponseEntity<Announcement> updateAnnouncement(@PathVariable Integer id,
                                                           @RequestBody Announcement announcementDetails) {
        return announcementRepository.findById(id)
                .map(announcement -> {
                    announcement.setTitle(announcementDetails.getTitle());
                    announcement.setContent(announcementDetails.getContent());
                    Announcement updatedAnnouncement = announcementRepository.save(announcement);
                    return ResponseEntity.ok(updatedAnnouncement);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Usunięcie ogłoszenia
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAnnouncement(@PathVariable Integer id) {
        return announcementRepository.findById(id)
                .map(announcement -> {
                    announcementRepository.delete(announcement);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
