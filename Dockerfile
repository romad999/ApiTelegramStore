# Шаг 1: Берем готовый образ с Java 21 на базе легкого Linux (Alpine)
FROM eclipse-temurin:21-jre-alpine

# Шаг 2: Указываем рабочую папку внутри контейнера
WORKDIR /app

# Шаг 3: Копируем наш скомпилированный jar-файл из папки target внутрь контейнера
# (После сборки Maven имя файла обычно выглядит как ApiTelegramStore-0.0.1-SNAPSHOT.jar)
COPY target/ApiTelegramStore-0.0.1-SNAPSHOT.jar app.jar

# Шаг 4: Говорим контейнеру, какой командой запускать наше приложение
ENTRYPOINT ["java", "-jar", "app.jar"]