package com.gabrielanceski.publicationscsvprocessor.repository;

import com.gabrielanceski.publicationscsvprocessor.domain.Publication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class PublicationRepository implements IPublicationRepository<Publication> {
    private static final Logger logger = LoggerFactory.getLogger(PublicationRepository.class);
    private final JdbcClient jdbcClient;


    public PublicationRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }


    @Override
    public List<Publication> findAll() {
        logger.debug("findAll() - Finding all publications.");
        return jdbcClient.sql("""
                SELECT * FROM PUBLICATIONS
            """)
            .query(Publication.class)
            .list();
    }

    @Override
    public Optional<Publication> findPublicationByDoi(String doi) {
        logger.debug("findByDoi() - Finding publication by DOI: <{}>", doi);
        return jdbcClient.sql("""
                SELECT * FROM PUBLICATIONS WHERE DOI = ?
            """)
            .param(doi)
            .query(Publication.class)
            .optional();
    }

    @Override
    public List<Publication> findAllPublicationsByYear(Integer year) {
        logger.debug("findByYear() - Finding publications by year: <{}>", year);
        return jdbcClient.sql("""
                SELECT * FROM PUBLICATIONS WHERE "YEAR" = ?
            """)
            .param(year)
            .query(Publication.class)
            .list();
    }

    @Override
    public Publication save(Publication publication) {
        logger.debug("save() - Saving publication: <{}>", publication);
        jdbcClient.sql("""
                INSERT INTO PUBLICATIONS (TITLE, AUTHORS, JOURNAL, "YEAR", PUBLISHED, EPUBLISHED, VOLUME, ISSUE, PAGES, DOI, PMID, LABELS, QUALIFIERS, IUID, URL, DOIURL, PUBMEDURL)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
            """)
            .param(publication.title())
            .param(publication.authors())
            .param(publication.journal())
            .param(publication.year())
            .param(publication.published())
            .param(publication.ePublished())
            .param(publication.volume())
            .param(publication.issue())
            .param(publication.pages())
            .param(publication.doi())
            .param(publication.pmid())
            .param(publication.labels())
            .param(publication.qualifiers())
            .param(publication.iuid())
            .param(publication.url())
            .param(publication.doiUrl())
            .param(publication.pubMedUrl())
        .update();
        return publication;
    }

    @Override
    public Collection<Publication> saveAll(Collection<Publication> publications) {
        logger.debug("saveAll() - Saving {} publications.", publications.size());
        // TODO: Esse método não é eficiente, pois faz uma chamada ao banco de dados para cada publicação, fazer um batch insert aqui seria mais eficiente...
        // mas por agora serve, e eu gostei do jdbcClient =)
        publications.forEach(this::save);
        return publications;
    }
}
