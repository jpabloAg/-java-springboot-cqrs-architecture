# java springboot cqrs architecture

### Abstract

Este proyecto es una aplicación desarrollada en Java con el framework Spring Boot utilizando la arquitectura CQRS (Command Query Responsibility Segregation). La arquitectura CQRS permite separar las operaciones de escritura (comandos) y lectura (consultas) en diferentes modelos de objetos y bases de datos, lo que puede mejorar la escalabilidad y la flexibilidad de la aplicación.

La aplicación incluye un conjunto de endpoints RESTful que se pueden consumir desde un cliente externo, los cuales permiten realizar operaciones de escritura y lectura en la aplicación.

Este proyecto se puede utilizar como base para desarrollar aplicaciones de diferentes tipos y escalas utilizando la arquitectura CQRS.

### Objetivos

El objetivo de este proyecto es proporcionar una arquitectura de sistema adecuada para aplicaciones que requieran una alta escalabilidad y un alto rendimiento. Para ello, se ha utilizado la arquitectura CQRS (Command Query Responsibility Segregation) en conjunto con PostgreSQL para las escrituras y Redis para las lecturas.

Además, se ha implementado una integración con Debezium para transmitir eventos a Kafka cada vez que se escriba en la base de datos PostgreSQL. De esta manera, se puede tener una visibilidad en tiempo real de las operaciones de escritura en la base de datos y proveer de un mecanismo para solventar la consistencia eventual en la base de datos de lectura.
