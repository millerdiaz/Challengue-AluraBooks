# Challengue-AluraBooks

## Descripción del Proyecto
Challengue-AluraBooks es una aplicación web desarrollada como parte de un desafío educativo para Alura Latam y Oracle Next Education. Su propósito es gestionar un catálogo de libros, permitiendo la consulta de información de libros obtenida desde la API pública Gutendex, así como la creación y administración de un catálogo personalizado almacenado en una base de datos relacional.

## Características
- **Consumo de la API de Gutendex:** Obtención de información de libros como títulos, autores, y detalles adicionales.
- **Catálogo Personalizado:** Creación y gestión de un catálogo propio de libros utilizando persistencia en PostgreSQL.
- **Variables de Entorno:** Configuración de las propiedades de la aplicación mediante variables de entorno para mayor seguridad y flexibilidad.
- **Práctica de Tecnologías Modernas:** Uso de Java y el framework Spring para implementar una arquitectura robusta y escalable.

## Tecnologías Utilizadas
- **Java 17:** Lenguaje de programación principal.
- **Spring Framework:** Para el desarrollo de la aplicación web, incluyendo:
  - Spring Boot: Configuración y despliegue rápido.
  - Spring Data JPA: Manejo de la persistencia de datos.
  - Spring Web: Consumo de APIs externas y desarrollo de controladores REST.
- **PostgreSQL:** Base de datos utilizada para almacenar el catálogo de libros.
- **Gutendex API:** Fuente externa de información sobre libros.
- **Maven:** Herramienta para la gestión de dependencias y configuración del proyecto.

## Configuración del Proyecto
### Requisitos Previos
1. **Java 17 o superior** instalado en su sistema.
2. **PostgreSQL** configurado y ejecutándose en su máquina local o en un servidor remoto.
3. Herramientas como **Maven** para la gestión de dependencias.
   

## Configuración de Dependencias

Asegúrese de que el archivo pom.xml incluye las siguientes dependencias:

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-annotations</artifactId>
        <version>2.13.0</version>
    </dependency>
</dependencies>

### Configuración de Variables de Entorno
Cree un archivo `.env` en el directorio raíz del proyecto o configure las siguientes variables de entorno en su sistema:

```
DB_URL=jdbc:postgresql://<host>:<puerto>/<nombre_base_datos>
DB_USERNAME=<usuario_postgres>
DB_PASSWORD=<contraseña_postgres>
API_BASE_URL=https://gutendex.com/books
```

Asegúrese de que `application.properties` en la carpeta `src/main/resources` esté configurado para usar estas variables de entorno:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
api.base.url=${API_BASE_URL}
```

### Instalación y Ejecución
1. Clone este repositorio:
   ```bash
   git clone https://github.com/millerdiaz/Challengue-AluraBooks.git
   ```
2. Acceda al directorio del proyecto:
   ```bash
   cd Challengue-AluraBooks
   ```
3. Instale las dependencias utilizando Maven:
   ```bash
   mvn install
   ```
4. Ejecute la aplicación:
   ```bash
   mvn spring-boot:run
   ```

### Uso
Una vez que la aplicación esté en ejecución, podrá acceder a las siguientes funcionalidades:
1. Consultar libros desde la API de Gutendex.
2. Agregar libros al catálogo personalizado.
3. Gestionar el catálogo mediante operaciones CRUD.

## Autor
- Miller Peña  
Este proyecto fue desarrollado como parte del programa educativo de **Alura Latam** en colaboración con **Oracle Next Education**.

---
© 2025 Miller Peña.

