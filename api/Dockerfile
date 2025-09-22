# https://docs.spring.io/spring-boot/reference/packaging/container-images/dockerfiles.html

# extract pre-built .jar
FROM bellsoft/liberica-openjre-debian:21-cds AS extract
WORKDIR /extract
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
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