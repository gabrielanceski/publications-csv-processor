package com.gabrielanceski.publicationscsvprocessor.domain;

import java.util.Objects;

public record Publication (
    String title,
    String authors,
    String journal,
    String year,
    String published,
    String ePublished,
    String volume,
    String issue,
    String pages,
    String doi,
    String pmid,
    String labels,
    String qualifiers,
    String iuid,
    String url,
    String doiUrl,
    String pubMedUrl
) implements GenericPublication {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Publication that = (Publication) o;
        return Objects.equals(doi, that.doi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doi);
    }
}
