package com.gabrielanceski.publicationscsvprocessor.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IPublicationRepository<T> {
    List<T> findAll();
    Optional<T> findPublicationByDoi(String doi);
    List<T> findAllPublicationsByYear(Integer year);
    T save(T publication);
    Collection<T> saveAll(Collection<T> publications);
}
