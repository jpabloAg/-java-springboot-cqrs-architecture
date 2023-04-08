# java springboot cqrs architecture

### Abstract

Este proyecto es una aplicación desarrollada en Java con el framework Spring Boot utilizando la arquitectura CQRS (Command Query Responsibility Segregation). La arquitectura CQRS permite separar las operaciones de escritura (comandos) y lectura (consultas) en diferentes modelos de objetos y bases de datos, lo que puede mejorar la escalabilidad y la flexibilidad de la aplicación.

La aplicación incluye un conjunto de endpoints RESTful que se pueden consumir desde un cliente externo, los cuales permiten realizar operaciones de escritura y lectura en la aplicación.

Este proyecto se puede utilizar como base para desarrollar aplicaciones de diferentes tipos y escalas utilizando la arquitectura CQRS.

### Objetivos

El objetivo de este proyecto es proporcionar una arquitectura de sistema adecuada para aplicaciones que requieran una alta escalabilidad y un alto rendimiento. Para ello, se ha utilizado la arquitectura CQRS (Command Query Responsibility Segregation) en conjunto con una arquitecura hexagonal usando PostgreSQL como base de datos relacional para las escrituras y Redis, una base de datos en memoria, para las lecturas.

Además, se ha implementado una integración con Debezium para transmitir eventos a Kafka cada vez que se escriba en la base de datos PostgreSQL. De esta manera, se puede tener una visibilidad en tiempo real de las operaciones de escritura en la base de datos y proveer de un mecanismo para solventar la consistencia eventual en la base de datos de lectura.

### Definiciones y Acrónimos

- **CDC:** CDC (Capture Data Change) es una técnica que permite capturar los cambios en las bases de datos en tiempo real, lo que permite tener una visibilidad de los cambios que se están produciendo en la base de datos. Esto es especialmente útil en entornos donde se requiere una alta disponibilidad de los datos y una respuesta inmediata ante los cambios en los mismos. Como dato interesante, CDC es una buena estrateg+ia cuando se quiere integrar aplicaciones legacy con aplicaciones modernas.

- **Debezium:** Debezium es una herramienta open source que permite capturar los cambios en las bases de datos en tiempo real y transmitirlos a sistemas de mensajería como Kafka. De esta forma, se puede tener una visibilidad en tiempo real de los cambios en la base de datos y reaccionar de manera inmediata ante ellos. Se lanzó en 2016 y actualmente es compatible con los siguientes DBMS: MySQL, PostgreSQL, MongoDB, SQL Server, Oracle e IBM Db2.

- **Kafka:** Kafka es una plataforma de streaming de datos open source que permite la transmisión de mensajes de manera rápida y confiable a través de diferentes sistemas. Permite la integración de diferentes fuentes de datos y la transmisión en tiempo real de los mismos a diferentes destinos, lo que la hace especialmente útil en entornos de big data y análisis en tiempo real.

- **PostgreSQL:** PostgreSQL es un sistema de gestión de bases de datos relacional open source que se destaca por su escalabilidad y fiabilidad.

- **Redis:** Redis es una base de datos en memoria open source.

### Arquitectura propuesta

```mermaid
graph LR;
    customerWriteDb[(PostgreSQL write store)]
    customerReadDb[(Redis read store)]
    SpringBoot_PublicApi-->CreateCustomer
    SpringBoot_PublicApi-->CreateOrder
    SpringBoot_PublicApi-->GetCustomers
    SpringBoot_PublicApi-->GetOrders
    GetCustomers-->customerReadDb
    GetOrders-->customerReadDb
    CreateCustomer-->|Write New Customer|customerWriteDb
    CreateOrder-->|Write New Order|customerWriteDb
    customerWriteDb-->|Event Detected|Debezium
    subgraph Kafka
    event3-->event2
    event2-->event1
    end
    Debezium-->Kafka
    Kafka-->SpringBoot_KafkaListener
    SpringBoot_KafkaListener-->|Write Projections|customerReadDb
    SpringBoot_KafkaListener-.->|Eventual Consistency|customerReadDb
```

### Infraestructura

La aplicación requiere de ciertos servicios para poder funcionar correctamente como las bases de datos, el conector de debezium y el message broker que para esta ocasión es kafka. Dichos servicios están definidos usando docker-compose.

```yaml
version: '3.5'

services:
  zookeeper:
    container_name: zookeeper
    image: docker.io/bitnami/zookeeper:3.7
    ports:
      - "2181:2181"
    environment:
      - ALLOW_ANONYMOUS_LOGIN=yes
  kafka:
    container_name: kafka
    image: docker.io/bitnami/kafka:2
    ports:
      - "9092:9092"
      - "29092:29092"
    environment:
      - KAFKA_CFG_LISTENERS=PLAINTEXT://:9092,EXTERNALPLAINTEXT://:29092
      - KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092,EXTERNALPLAINTEXT://localhost:29092
      - KAFKA_LISTENER_SECURITY_PROTOCOL_MAP=PLAINTEXT:PLAINTEXT,EXTERNALPLAINTEXT:PLAINTEXT
      - KAFKA_INTER_BROKER_LISTENER_NAME=EXTERNALPLAINTEXT
      - KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
      - ALLOW_PLAINTEXT_LISTENER=yes
    depends_on:
      - zookeeper
  postgres:
    container_name: postgres
    image: postgres:11
    environment:
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres
      POSTGRES_DB: postgresdb
      PGDATA: /data/postgres
    ports:
      - "5432:5432"

  debezium:
    container_name: debezium
    image: debezium/connect
    environment:
      GROUP_ID: 1
      CONFIG_STORAGE_TOPIC: my-connect-configs
      OFFSET_STORAGE_TOPIC: my-connect-offsets
      BOOTSTRAP_SERVERS: kafka:9092
      ADVERTISED_HOST_NAME: debezium
    ports:
      - "8083:8083"
    depends_on:
      - kafka

  redis:
    container_name: redis
    image: redis:7.0.5-alpine
    ports:
      - "6379:6379"
    command: [ "redis-server", "--requirepass", "SUPER_SECRET_PASSWORD" ]
 ```
 
 ### Referencias
 
 - ["Using Debezium, CDC for Apache Kafka, with PostgreSQL and MongoDB"](https://blog.palark.com/debezium-cdc-for-apache-kafka/) articulo, Renat Khamadiev
 - ["Patrones de diseño en la nube - Patrón CQRS"](https://learn.microsoft.com/es-es/azure/architecture/patterns/cqrs) blog, Microsoft
 - ["Guide to Setting Up Apache Kafka Using Docker"](https://www.baeldung.com/ops/kafka-docker-setup) Guia, Baeldung
