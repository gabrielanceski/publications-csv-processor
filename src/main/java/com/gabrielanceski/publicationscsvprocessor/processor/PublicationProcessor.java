package com.gabrielanceski.publicationscsvprocessor.processor;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PublicationProcessor<I, O> {

    void processFile(MultipartFile file);

    Optional<O> mapPublicationInput(I input);

}
