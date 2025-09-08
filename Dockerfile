ARG BASE_IMAGE=docker.io/amazoncorretto:21-al2023
FROM ${BASE_IMAGE} as builder
WORKDIR /application
ARG JAR_FILE
COPY target/${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract

FROM ${BASE_IMAGE}
WORKDIR /application
USER 1000
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/snapshot-dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/application/ ./

ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher"]