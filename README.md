# REST API сервис списка задач

---

Разработка: **Spring Boot** + **Spring Validation** + **Lombok** + **Spring JPA** + **PostgreSQL** + **Flyway**

Тестирование: **Mockito** + **JUnit5** + **H2 Database**

---

## Note

**GET /api/v1/notes/all** - получение всех записей

**GET /api/v1/notes/{id}** - получение записи по идентификатору

**POST /api/v1/notes** - добавление новой записи

**PATCH /api/v1/notes/{id}?title={title}** - обновление наименования записи по идентификатору и передаваемому
наименованию в параметре

**DELETE /api/v1/notes/{id}** - удаление записи по идентификатору

---

## Task

**POST /api/v1/tasks** - добавление новой задачи

**DELETE /api/v1/tasks/{id}** - удаление задачи по идентификатору

**PATCH /api/v1/tasks/{id}/changeStatus** - обновление статуса задачи по идентификатору