# Используем официальный образ OpenJDK
FROM openjdk:23-jdk-slim

# Обновляем пакеты и устанавливаем curl
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Указываем рабочую директорию
WORKDIR /app

# Копируем JAR-файл (созданный Gradle-сборкой) в контейнер
COPY build/libs/*.jar app.jar

COPY entrypoint.sh .
RUN chmod +x entrypoint.sh

# Открываем порт, на котором будет работать приложение
EXPOSE 8082

# Команда для запуска приложения
ENTRYPOINT ["./entrypoint.sh"]
