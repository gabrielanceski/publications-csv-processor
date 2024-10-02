package com.gabrielanceski.publicationscsvprocessor.service;

import com.gabrielanceski.publicationscsvprocessor.domain.Publication;
import com.gabrielanceski.publicationscsvprocessor.repository.IPublicationRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class PublicationExportService {
    private final IPublicationRepository<Publication> publicationRepository;

    public PublicationExportService(IPublicationRepository<Publication> publicationRepository) {
        this.publicationRepository = publicationRepository;
    }

    public byte[] exportData() throws IOException {
        List<Publication> entities = publicationRepository.findAll(); // Busque os dados do banco de dados

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        // Criar cabeçalhos
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("title");
        headerRow.createCell(1).setCellValue("authors");
        headerRow.createCell(2).setCellValue("journal");
        headerRow.createCell(3).setCellValue("year");
        headerRow.createCell(4).setCellValue("published");
        headerRow.createCell(5).setCellValue("ePublished");
        headerRow.createCell(6).setCellValue("volume");
        headerRow.createCell(7).setCellValue("issue");
        headerRow.createCell(8).setCellValue("pages");
        headerRow.createCell(9).setCellValue("doi");
        headerRow.createCell(10).setCellValue("pmid");
        headerRow.createCell(11).setCellValue("labels");
        headerRow.createCell(12).setCellValue("qualifiers");
        headerRow.createCell(13).setCellValue("iuid");
        headerRow.createCell(14).setCellValue("url");
        headerRow.createCell(15).setCellValue("doiUrl");
        headerRow.createCell(16).setCellValue("pubMedUrl");

        // Adicione mais cabeçalhos conforme necessário

        // Preencher os dados
        int rowNum = 1;
        for (Publication entity : entities) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entity.title());
            row.createCell(1).setCellValue(entity.authors());
            row.createCell(2).setCellValue(entity.journal());
            row.createCell(3).setCellValue(entity.year());
            row.createCell(4).setCellValue(entity.published());
            row.createCell(5).setCellValue(entity.ePublished());
            row.createCell(6).setCellValue(entity.volume());
            row.createCell(7).setCellValue(entity.issue());
            row.createCell(8).setCellValue(entity.pages());
            row.createCell(9).setCellValue(entity.doi());
            row.createCell(10).setCellValue(entity.pmid());
            row.createCell(11).setCellValue(entity.labels());
            row.createCell(12).setCellValue(entity.qualifiers());
            row.createCell(13).setCellValue(entity.iuid());
            row.createCell(14).setCellValue(entity.url());
            row.createCell(15).setCellValue(entity.doiUrl());
            row.createCell(16).setCellValue(entity.pubMedUrl());
        }

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } finally {
            workbook.close();
        }
    }

}
