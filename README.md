# Insurance Service

Este projeto é uma aplicação Spring Boot para gerenciamento de cotações de seguros. Ele integra com RabbitMQ para comunicação assíncrona e utiliza PostgreSQL como banco de dados para armazenar informações persistentes.

## Funcionalidades

- **Criação de Cotações**: A aplicação permite criar e gerenciar cotações de seguros, se comunicando com um serviço externo.
- **RabbitMQ**: Comunicação assíncrona entre os serviços através de filas.
- **PostgreSQL**: Armazenamento persistente de dados das cotações e políticas de seguro.
- **Interface Web**: Interface simples para interagir com o RabbitMQ através da UI do RabbitMQ Management.

## Arquitetura

A arquitetura da aplicação é composta por:

- **Spring Boot**: Framework principal para desenvolvimento da aplicação.
- **RabbitMQ**: Sistema de filas para comunicação assíncrona.
- **PostgreSQL**: Banco de dados relacional para armazenar dados persistentes.
- **Docker**: Ambiente de contêiner para fácil deploy e escalabilidade dos serviços.

## Tecnologias Utilizadas

- **Java 17**: Versão do Java utilizada no projeto.
- **Spring Boot 3.x**: Framework para criação da aplicação Java.
- **RabbitMQ**: Sistema de filas para comunicação assíncrona.
- **PostgreSQL 13**: Banco de dados relacional.
- **Docker**: Contêinerização dos serviços para ambiente controlado.
- **JPA/Hibernate**: ORM para interação com o banco de dados.
- **JUnit & Mockito**: Frameworks para testes unitários e mock de dependências.

## Como Rodar a Aplicação

### Pré-requisitos

Certifique-se de ter os seguintes softwares instalados:

- Docker (para rodar a aplicação e dependências em contêineres)
- Docker Compose (para orquestrar os contêineres)
- Acesso à internet para consultar os produtos e ofertas (mock server)

### Rodando com Docker

1. Clone o repositório:

   ```bash
   git clone https://github.com/raphaelzana/insurance.git
   cd insurance
   ```

2. Construa e suba os contêineres com Docker Compose:

   ```bash
   docker-compose up --build
   ```

3. Acesse a aplicação via:

   - **API**: `http://localhost:8089`
   - **RabbitMQ UI**: `http://localhost:15672` (credenciais padrão: usuário: `user`, senha: `password`)

4. Os serviços serão iniciados nos seguintes contêineres:
   - `rabbitmq`: Servidor de mensageria RabbitMQ.
   - `postgres`: Banco de dados PostgreSQL.
   - `java_app`: Aplicação Spring Boot.

5. Para parar os contêineres, execute:

   ```bash
   docker-compose down
   ```

### Variáveis de Ambiente

As variáveis de ambiente são configuradas no `docker-compose.yml` e no `application.properties`. Aqui estão os principais valores utilizados:

- **PostgreSQL**:
  - `POSTGRES_USER`: admin
  - `POSTGRES_PASSWORD`: adminpassword
  - `POSTGRES_DB`: meubanco

- **RabbitMQ**:
  - `RABBITMQ_DEFAULT_USER`: user
  - `RABBITMQ_DEFAULT_PASS`: password

- **Aplicação Spring Boot**:
  - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://postgres:5432/meubanco`
  - `SPRING_DATASOURCE_USERNAME`: admin
  - `SPRING_DATASOURCE_PASSWORD`: adminpassword
  - `RABBITMQ_HOST`: rabbitmq
  - `RABBITMQ_USERNAME`: user
  - `RABBITMQ_PASSWORD`: password

## Testes

Para rodar os testes unitários, execute o comando:

```bash
mvn test
```

Este comando irá executar os testes definidos no projeto, utilizando o JUnit e o Mockito para validar o comportamento das funcionalidades implementadas.

## Endpoints Disponíveis

### 1. Criar Citação

- **POST** `/api/v1/insurance/quote`
- **Descrição**: Cria uma nova cotação de seguro.
- **Exemplo de Body**:

  ```json
  {
        "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587", 
        "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c", 
        "category": "HOME", 
        "total_monthly_premium_amount": 75.25, 
        "total_coverage_amount": 825000.00, 
        "coverages": { 
        "Incêndio": 250000.00, 
        "Desastres naturais": 500000.00, 
        "Responsabiliadade civil": 75000.00 
        }, 
        "assistances": [ 
        "Encanador", 
        "Eletricista", 
        "Chaveiro 24h" 
        ], 
        "customer": { 
            "document_number": "36205578900", 
            "name": "John Wick", 
            "type": "NATURAL", 
            "gender": "MALE", 
            "date_of_birth": "1973-05-02", 
            "email": "johnwick@gmail.com", 
            "phone_number": 11950503030 
        } 
    }
  ```


A mensagem será postada na fila **quote-received**


### 2. Receber Retorno da Cotação

- **Descrição**: A aplicação consome mensagens da fila **policy-created** para receber e persistir a apolice no banco de dados.
- **Exemplo de Body**:

    ```json
    {
        "id": 22345, 
        "insurance_policy_id": 756969, 
        "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587", 
        "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c", 
        "category": "HOME", 
        "created_at": "2024-05-22T20:37:17.090098", 
        "updated_at": "2024-05-22T21:05:02.090098", 
        "total_monthly_premium_amount": 75.25, 
        "total_coverage_amount": 825000.00, 
        "coverages": { 
        "Incêndio": 250000.00, 
        "Desastres naturais": 500000.00, 
        "Responsabiliadade civil": 75000.00 
        }, 
        "assistances": [ 
        "Encanador", 
        "Eletricista", 
        "Chaveiro 24h" 
        ], 
        "customer": { 
            "document_number": "36205578900", 
            "name": "John Wick", 
            "type": "NATURAL", 
            "gender": "MALE", 
            "date_of_birth": "1973-05-02", 
            "email": "johnwick@gmail.com", 
            "phone_number": 11950503030 
        }
    } 
    ```

A mensagem será consumida da fila **policy-created** e a cotação será persistida no banco de dados.

### 3. Consultar Cotações

- **GET** `/api/v1/insurance/quote/{id}`
- **Descrição**: Consulta a cotação de seguros armazenadas por ID.
- **Exemplo de Response**:

    ```json
    {
        "id": 22345, 
        "insurance_policy_id": 756969, 
        "product_id": "1b2da7cc-b367-4196-8a78-9cfeec21f587", 
        "offer_id": "adc56d77-348c-4bf0-908f-22d402ee715c", 
        "category": "HOME", 
        "created_at": "2024-05-22T20:37:17.090098", 
        "updated_at": "2024-05-22T21:05:02.090098", 
        "total_monthly_premium_amount": 75.25, 
        "total_coverage_amount": 825000.00, 
        "coverages": { 
        "Incêndio": 250000.00, 
        "Desastres naturais": 500000.00, 
        "Responsabiliadade civil": 75000.00 
        }, 
        "assistances": [ 
        "Encanador", 
        "Eletricista", 
        "Chaveiro 24h" 
        ], 
        "customer": { 
            "document_number": "36205578900", 
            "name": "John Wick", 
            "type": "NATURAL", 
            "gender": "MALE", 
            "date_of_birth": "1973-05-02", 
            "email": "johnwick@gmail.com", 
            "phone_number": 11950503030 
        }
    }
    ```