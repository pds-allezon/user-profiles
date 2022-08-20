FROM openjdk:17-alpine

COPY . /
RUN ./gradlew build
ENTRYPOINT ["java", "-jar", "/build/libs/user-profiles-0.0.1-SNAPSHOT.jar"]
