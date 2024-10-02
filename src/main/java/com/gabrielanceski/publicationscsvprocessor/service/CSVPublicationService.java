package com.gabrielanceski.publicationscsvprocessor.service;

import com.gabrielanceski.publicationscsvprocessor.processor.PublicationProcessor;
import com.gabrielanceski.publicationscsvprocessor.repository.IPublicationRepository;
import com.gabrielanceski.publicationscsvprocessor.controller.PublicationController;
import com.gabrielanceski.publicationscsvprocessor.domain.Publication;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CSVPublicationService implements IPublicationService<Publication> {
    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);
    private final IPublicationRepository<Publication> publicationRepository;
    private final PublicationProcessor<CSVRecord, Publication> csvProcessor;

    public CSVPublicationService(IPublicationRepository<Publication> publicationRepository, PublicationProcessor<CSVRecord, Publication> csvProcessor) {
        this.publicationRepository = publicationRepository;
        this.csvProcessor = csvProcessor;
    }

    @Override
    public void processFile(MultipartFile file) {
        logger.info("processFile() - START - Processing CSV file. File size (bytes): <{}>", file.getSize());
        if (file.isEmpty()) {
            logger.error("processFile() - END - Empty file.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo vazio.");
        }

        csvProcessor.processFile(file);
    }

    @Override
    public Publication findByDOI(String doi) {
        return publicationRepository.findPublicationByDoi(doi).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "DOI não encontrado."));
    }

    @Override
    public List<Publication> findPublicationsByYear(Integer year) {
        return publicationRepository.findAllPublicationsByYear(year);
    }

}
