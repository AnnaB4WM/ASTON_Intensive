# Используем официальный образ OpenJDK 17 для базы
FROM openjdk:17-jdk-slim

# Устанавливаем рабочий каталог внутри контейнера
WORKDIR /app

# Скопируем JAR файл в контейнер
ARG JAR_FILE=target/user-service.jar
COPY ${JAR_FILE} user-service.jar

# Выполняем JAR файл при запуске контейнера
ENTRYPOINT ["java", "-jar", "user-service.jar"]