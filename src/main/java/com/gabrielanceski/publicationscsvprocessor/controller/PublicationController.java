package com.gabrielanceski.publicationscsvprocessor.controller;

import com.gabrielanceski.publicationscsvprocessor.domain.Publication;
import com.gabrielanceski.publicationscsvprocessor.service.IPublicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/publications")
public class PublicationController {
    private final IPublicationService<Publication> publicationService;

    public PublicationController(IPublicationService<Publication> publicationService) {
        this.publicationService = publicationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Void> processFile(@RequestParam MultipartFile file) {
        publicationService.processFile(file);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Publication> findPublicationByDOI(@RequestParam String doi) {
        return ResponseEntity.ok(publicationService.findByDOI(doi));
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Publication>> findPublicationsByYear(@PathVariable Integer year) {
        return ResponseEntity.ok(publicationService.findPublicationsByYear(year));
    }
}
