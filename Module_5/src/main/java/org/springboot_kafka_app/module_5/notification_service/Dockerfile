# Используем официальный образ OpenJDK 17 для базы
FROM openjdk:17-jdk-slim

# Устанавливаем рабочий каталог внутри контейнера
WORKDIR /app

# Скопируем JAR файл в контейнер
ARG JAR_FILE=target/notification-service.jar
COPY ${JAR_FILE} notification-service.jar

# Выполняем JAR файл при запуске контейнера
ENTRYPOINT ["java", "-jar", "notification-service.jar"]