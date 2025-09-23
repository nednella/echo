# https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html

# build container
FROM bellsoft/liberica-openjdk-debian:21 AS build
WORKDIR /src
COPY gradlew ./
COPY gradle gradle
RUN chmod +x gradlew
COPY settings.gradle build.gradle ./
COPY src src
RUN ./gradlew --no-daemon assemble

# extract container
FROM bellsoft/liberica-openjre-debian:21-cds AS extract
WORKDIR /extract
COPY --from=build /src/build/libs/*.jar application.jar
RUN java -Djarmode=tools -jar application.jar extract --layers --destination extracted

# runtime container
FROM bellsoft/liberica-openjre-debian:21-cds
WORKDIR /application
COPY --from=extract /extract/extracted/dependencies/ ./
COPY --from=extract /extract/extracted/spring-boot-loader/ ./
COPY --from=extract /extract/extracted/snapshot-dependencies/ ./
COPY --from=extract /extract/extracted/application/ ./

ENV JVM_OPTIONS=""
ENTRYPOINT ["sh","-c","java $JVM_OPTIONS -jar application.jar"]