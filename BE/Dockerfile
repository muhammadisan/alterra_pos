FROM --platform=linux/amd64 openjdk:17
WORKDIR /app
COPY target/pos-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java", "-jar","pos-0.0.1-SNAPSHOT.jar"]