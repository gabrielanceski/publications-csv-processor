# Processamento de CSV de publicações

## Objetivo 

Desenvolver um serviço RESTful que recebe via POST um
arquivo CSV (https://publications.scilifelab.se/publications/csv),
processa e salva no banco de dados. Desenvolver outro serviço GET que recebe como parâmetro o DOI e retorna um JSON com os respectivos dados. Desenvolver um terceiro serviço que recebe
o ano e gera um JSON com todos os itens deste ano.
Desenvolver um quarto serviço que exporta em Excel todos os
dados.
Tecnologias a serem utilizadas:
Backend: Spring Boot, Apache POI
Banco de dados: H2
Testes Unitários: JUnit

## CURLs para testes

Upload de arquivo CSV
```
curl -L 'http://localhost:8080/publications/upload' \
-F 'file=@"arquivo.csv"'
```

Buscar detalhes de publicação pelo DOI
```
curl -L 'http://localhost:8080/publications?doi=10.1126%2Fsciadv.adn0597'
```

Buscar publicações por ano
```
curl -L 'http://localhost:8080/publications/year/ano'
curl -L 'http://localhost:8080/publications/year/2020'
```

Exportar publicações para Excel
```
curl -L 'http://localhost:8080/publications/download'
```