package com.gabrielanceski.publicationscsvprocessor.processor;

import com.gabrielanceski.publicationscsvprocessor.domain.Publication;
import com.gabrielanceski.publicationscsvprocessor.repository.IPublicationRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;

@Component
public class CSVPublicationProcessor implements PublicationProcessor<CSVRecord, Publication> {
    private static final Logger logger = LoggerFactory.getLogger(CSVPublicationProcessor.class);
    private final IPublicationRepository<Publication> publicationRepository;

    @Value("${processing.chunk_size}") private int CHUNK_SIZE;

    public CSVPublicationProcessor(IPublicationRepository<Publication> publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    @Override
    public void processFile(MultipartFile csvFile) {
        List<CSVRecord> chunk = new ArrayList<>();
        int chunkCount = 0;
        int chunkRecordsCount = 0;

        try (Reader reader = new BufferedReader(new InputStreamReader(csvFile.getInputStream()))) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .build()
                    .parse(reader);
            int recordCounter = 0;

            for (CSVRecord record : records) {
                chunk.add(record);
                chunkRecordsCount++;
                recordCounter++;

                if (chunkRecordsCount == CHUNK_SIZE) {
                    chunkCount++;
                    processChunk(chunk, chunkCount);
                    chunkRecordsCount = 0;
                    chunk.clear();
                }
            }

            logger.info("processFile() - END - {} records processed.", recordCounter);
        } catch (IllegalArgumentException e) {
            logger.error("processFile() - END - Invalid file format.", e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato do arquivo inválido: " + e.getMessage());
        } catch (Exception e) {
            logger.error("processFile() - END - Error processing CSV file.", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao processar arquivo CSV: " + e.getMessage());
        }
    }

    private void processChunk(List<CSVRecord> chunk, int chunkNumber) {
        logger.info("processChunk() - Processing chunk with <{}> records.", chunk.size());

        // Usar HashSet para evitar que tenham DOIs repetidos no chunk, mas isso não impede de já haver um DOI repetido no banco de dados.
        Set<Publication> persistList = new HashSet<>();

        chunk.forEach(record -> {
            try {
                logger.debug("processChunk() - Processing number {} | record: <{}>", record.getRecordNumber(), record);
                mapPublicationInput(record).ifPresent(persistList::add);
            } catch (Exception e) {
                logger.error("processChunk() - Error processing record number {}. Chunk number {}. Cause: <{}>", record.getRecordNumber(), chunkNumber, e.getMessage());
            }
        });

        try {
            publicationRepository.saveAll(persistList);
        } catch (DuplicateKeyException e) {
            logger.warn("processChunk() - Duplicated DOI found on chunk {}. Skipping repeated records.", chunkNumber);
        } catch (Exception e) {
            logger.error("processChunk() - Error to save chunk number {}. Cause: <{}>", chunkNumber, e.getMessage());
        }

        logger.info("processChunk() - {} records persisted.", persistList.size());
    }

    @Override
    public Optional<Publication> mapPublicationInput(CSVRecord input) {
        String doi = input.get("DOI");

        if (doi == null || doi.isEmpty()) {
            logger.warn("mapPublicationInput() - DOI not found, skipping record. | number: {} | record: <{}>", input.getRecordNumber(), input);
            return Optional.empty();
        }

        Publication publication = new Publication(
            input.get("Title"),
            input.get("Authors"),
            input.get("Journal"),
            input.get("Year"),
            input.get("Published"),
            input.get("E-published"),
            input.get("Volume"),
            input.get("Issue"),
            input.get("Pages"),
            doi,
            input.get("PMID"),
            input.get("Labels"),
            input.get("Qualifiers"),
            input.get("IUID"),
            input.get("URL"),
            input.get("DOI URL"),
            input.get("PubMed URL")
        );

        logger.debug("mapPublicationInput() - Mapping to Publication: <{}>", publication);
        return Optional.of(publication);
    }
}
