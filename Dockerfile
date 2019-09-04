
FROM maven:latest AS appserver
WORKDIR /usr/src/movie-search
COPY pom.xml .
RUN mvn -B -f pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
COPY . .
RUN mvn -B -s /usr/share/maven/ref/settings-docker.xml package

FROM java:8-jdk-alpine
WORKDIR /movie-search-api
EXPOSE 8888
COPY --from=appserver /usr/src/movie-search/target/movie-search-api-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "/movie-search-api/movie-search-api-0.0.1-SNAPSHOT.jar"]
