FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

ENV TZ=Asia/Seoul

COPY build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]