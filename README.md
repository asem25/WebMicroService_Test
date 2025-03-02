# Микросервис для управления пользователями и подписками

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/technologies/javase/17-relnote-issues.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/database-PostgreSQL-blue)](https://www.postgresql.org/)
[![Docker](https://img.shields.io/badge/container-Docker-blue)](https://www.docker.com/)

Добро пожаловать в репозиторий микросервиса на **Spring Boot 3** (Java 17), который предоставляет REST API для работы с пользователями и их подписками.

---

## ✨ Возможности проекта

- **Управление пользователями**: создание, получение, обновление, удаление.
- **Управление подписками**: добавление подписок, получение списка, удаление.
- **Глобальный контроллер подписок**: получение ТОП-3 самых популярных подписок(по количеству пользователей).
- **Интеграция с PostgreSQL**.
- **Docker & Docker Compose** для удобного развёртывания.
- **Тесты** (JUnit + Mockito) для контроллеров и сервисов.
- **Глобальный обработчик ошибок** через `GlobalAdviceController`.

---

## ⚙️ Структура проекта

```bash
├── src
│   ├── main
│   │   ├── java/ru/semavin/microservice
│   │   │   ├── controllers   # REST-контроллеры
│   │   │   ├── dtos          # DTO-классы
│   │   │   ├── models        # JPA-сущности
│   │   │   ├── repositrories # Интерфейсы репозиториев
│   │   │   ├── services      # Сервисный слой
│   │   │   └── util          # Утилиты, фабрика исключений и т.д.
│   └── test
│       └── java/ru/semavin/microservice
│           ├── controllers   # Тесты для контроллеров
│           └── services      # Тесты для сервисов
├── Dockerfile               # Докер-файл для сборки образа
├── docker-compose.yml       # Docker Compose для запуска с PostgreSQL
├── pom.xml
└── README.md
```

---

## 🚀 Как запустить

1. **Склонируйте** репозиторий:
   ```bash
   git clone https://github.com/asem25/WebMicroService_Test.git
   ```
2. **Перейдите** в директорию проекта:
   ```bash
   cd microservice
   ```
3. **Соберите** проект (Maven):
   ```bash
   ./mvnw package -DskipTests
   ```
4. **Запустите** с помощью Docker Compose:
   ```bash
   docker-compose up --build
   ```
5. Приложение будет доступно по адресу: [http://localhost:8080](http://localhost:8080)

---

## 🌐 Endpoints
Ниже краткая сводка эндпоинтов (префикс`/api/v1`):

| Метод | URI                                | Описание                                       |
|-------|------------------------------------|------------------------------------------------|
| POST  | `/users`                           | Создать пользователя                           |
| GET   | `/users/{id}`                      | Получить пользователя по ID                    |
| PUT   | `/users/{id}`                      | Обновить данные пользователя                   |
| DELETE| `/users/{id}`                      | Удалить пользователя                           |
| GET   | `/users`                           | Получить список всех пользователей             |
| POST  | `/users/{id}/subscriptions`        | Добавить подписку пользователю                 |
| GET   | `/users/{id}/subscriptions`        | Получить все подписки пользователя             |
| DELETE| `/users/{id}/subscriptions/{subId}`| Удалить подписку по ID                         |
| GET   | `/subscriptions/top`               | Получить ТОП-3 самых популярных подписок       |

---

## 🛠️ Стек
- **Java 17**
- **Spring Boot 3** (Web, Data JPA, Validation)
- **PostgreSQL** (через Spring Data)
- **Maven** / **Gradle** (для сборки проекта)
- **JUnit** + **Mockito** (тестирование)
- **Docker** + **Docker Compose** (развёртывание)
- **SLF4J** + **Lombok @Slf4j** (логирование)
- **Bean Validation** (@NotBlank, @Email и т.д.)

---
## 📚 Документация

В проекте подключен OpenAPI/Swagger (по умолчанию в Spring Boot 3). Для просмотра UI-документации после запуска приложения перейдите по ссылке:
[Swagger UI](http://localhost:8080/swagger-ui/index.html#/)

## 📧 Контакты
Если возникли вопросы или предложения по улучшению, создавайте **Issue** в данном репозитории или пишите на почту:
`asemavin250604@gmail.com`

---

**Подпись автора:**

Александр (aka. asem25)

[Github](https://github.com/asem25)

[Telegram](https://t.me/asem250604)