# Processamento de CSV de publicações

## Objetivo 

Esse projeto surgiu como um teste público para execução de um trabalho de extensão do Instituto Federal do Rio Grande do Sul.
As informações do projeto estão disponíveis no [EDITAL PROPPI Nº 19/2024 – SELEÇÃO DE BOLSISTAS PARA O PROJETO DE DESENVOLVIMENTO DA REDE INTEGRA E DO PORTAL INTEGRA](https://ifrs.edu.br/editais/edital-proppi-no-19-2024-selecao-de-bolsistas-para-o-projeto-de-desenvolvimento-da-rede-integra-e-do-portal-integra/).

O desenvolvimento foi feito puramente para praticar alguns padrões de projeto e tecnologias, sem a intenção de ser utilizado em produção ou participar do processo seletivo.

### Requisitos

Realizar o desenvolvimento de um sistema que permita a importação de publicações científicas a partir de um arquivo CSV, e que permita a busca de publicações por DOI e por ano.

Serviços a serem desenvolvidos (endpoints):
- Desenvolver um serviço RESTful que recebe via POST um arquivo CSV do [link](https://publications.scilifelab.se/publications/csv), processa e salva no banco de dados. 
- Desenvolver um serviço GET que recebe como parâmetro o DOI e retorna um JSON com os respectivos dados.
- Desenvolver um terceiro serviço que recebe o ano e gera um JSON com todos os itens deste ano.
- Desenvolver um quarto serviço que exporta em Excel todos os dados.

Tecnologias a serem utilizadas:
- Backend: Spring Boot, Apache POI
- Banco de dados: H2
- Testes Unitários: JUnit

## Instruções para execução

Para executar o projeto, é necessário ter o Java e o Maven instalados. E então, executar os comandos abaixo:

```
mvn clean install package -DskipTests
java -jar target/publications-1.0.jar
```

O projeto estará disponível em http://localhost:8080

Como a aplicação utiliza um banco de dados em memória, os dados são perdidos ao reiniciar a aplicação. Os dados de exemplo podem ser encontrados na url mencionada na seção de requisitos.

### CURLs para testes

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