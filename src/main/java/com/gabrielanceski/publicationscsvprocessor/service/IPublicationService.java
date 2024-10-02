package com.gabrielanceski.publicationscsvprocessor.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPublicationService<T> {

    void processFile(MultipartFile file);

    T findByDOI(String doi);

    List<T> findPublicationsByYear(Integer year);

}
