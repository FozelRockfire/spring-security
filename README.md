# spring-security
Базовое веб-приложение с использованием Spring Security и JWT для аутентификации и авторизации пользователей. 

В приложении реализована базовая конфигурация Spring Security, для аутентификации пользователей используется JWT. Сохранение пользователей происходит в базу данных PostgreSQL.
Реализована базовая поддержку ролей пользователей, состоящая из ролей `USER` и `ADMIN`

Для просмотра swagger необходимо запустить приложение и перейти по ссылке: _http://localhost:8081/swagger-ui/index.html_

Контроллеры и сервисы покрыты unit-тестами.

Для старта приложения необходимо запустить postgreSQL через docker.compose или изменить настройки подключений к бд в application.yml

Стек технологий:
`Java 17` `Maven` `Spring` `Spring Security` `PostgreSQL` `Flyway`

## Автор:<br>
Попов Илья ([FozelRockfire](https://github.com/FozelRockfire))<br>
