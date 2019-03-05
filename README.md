# VTUBE

This is our final group project, assigned by **IT Talents**, on the website [youtube.com](https://www.youtube.com/).

IMPORTANT:
You can configure your application.properties file (path: src\main\resources) using the below:

server.port={port where you want application to listen for requests}(default is 8080)
spring.batch.initialize-schema=always
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql:{path tou your database (example: //localhost:3306/{database name})}?createDatabaseIfNotExist=true
spring.datasource.username={your username}
spring.datasource.password={your password}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5InnoDBDialect
spring.jpa.show-sql=true
