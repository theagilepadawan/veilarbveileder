FROM maven as builder
ADD / /source
WORKDIR /source
RUN mvn package -DskipTests

FROM navikt/java:8-appdynamics
ENV APPD_ENABLED=true
COPY --from=builder /source/target/veilarbveileder /app