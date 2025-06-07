# Проект для тестирования производительности Spring Boot

Для тестирования используем [Grafana K6](https://grafana.com/docs/k6/latest/)

Алгоритм запуска всегда одинаковый, на холодную, перед тестированием отключаем
отладочные логи и измерения

```
k6.exe --vus=20 --duration=100s .\<test-script>.js
```

[Swagger UI](http://localhost:8112/swagger-ui/index.html)

## Этапы:

### Этап 1

- Инициализация проекта
- Создание контроллера генерирующего сообщения
- Тестирование максимальной производительности
- Тестирование максимальной производительности при искусственно заданной задержке

### Этап 2

- Добавление типовых моделей JPA, Репозиториев, Сервисов
- Генерация данных в БД
- Тестирование максимальной производительности
- Тестирование максимальной производительности при искусственно заданной задержке

По моделям имеем следующее - Сотрудник, он же User работает в подразделении и может
совмещать несколько должностей, при этом должности классифицируются на "управленческие"
и другие, если у сотрудника имеется хотя бы одна управленческая должность мы считаем
сотрудника "менеджером".

### Этап 3

- Экспериментируем с relations, переписываем запросы
- Отключаем OSIV

### Этап 4

- Увеличиваем производительность
- Не забудьте сделать ANALYZE на табличках с данными

### Этап 5

- Максимизируем производительность
- Native sql в помощь

### Final Benchmarks

| Script                               | Requests | Rps           | OSIV | QUERIES | REASON                 |
|--------------------------------------|----------|---------------|------|---------|------------------------|
| jpa-user-with-relations-separated.js | 112183   | 1121.712831/s | NO   | 3       | BETTER in JPA queries  |
| jpa-user-with-relations-separated.js | 109186   | 1091.666174/s | YES  | 3       | BETTER for OSIV        |
| jpa-user-with-relations.js           | 4722     | 47.064806/s   | NO   | 2       | In memory pagination   |
| jpa-user-with-relations.js           | 4825     | 48.109755/s   | YES  | 2       | In memory pagination   |
| jpa-user-with-work-after.js          | 12752    | 127.426923/s  | NO   | 2+N     | No CPU                 |
| jpa-user-with-work-after.js          | 6460     | 64.459638/s   | YES  | 2+N     | No CPU and connections |
| jpa-user-with-work-before.js         | 12836    | 128.229522/s  | NO   | 2+N     | No CPU                 |
| jpa-user-with-work-before.js         | 12877    | 128.700342/s  | YES  | 2+N     | No CPU                 |
| jpa-user-with-work-parallel.js       | 18384    | 183.640275/s  | NO   | 2+N     | No CPU                 |
| jpa-user-with-work-parallel.js       | 18303    | 182.835649/s  | YES  | 2+N     | No CPU                 |
| jpa-user-with-work-parallel.js 10ms  | 30557    | 305.311594/s  | NO   | 2+N     | no connections and N+1 |
| jpa-user-with-work-parallel.js 10ms  | 30018    | 300.011141/s  | YES  | 2+N     | no connections and N+1 |
| simple-message-only-string.js        | 18441    | 184.220742/s  | NO   | 0       | No CPU                 |
| jpa-user.js                          | 31255    | 312.413579/s  | NO   | 2+N     | no connections and N+1 |
| jpa-user.js                          | 30739    | 307.231052/s  | YES  | 2+N     | no connections and N+1 |
| jooq-user.js                         | 128656   | 1286.418942/s | NO   | 2       |                        |
| jooq-user.js                         | 125668   | 1256.573425/s | YES  | 2       |                        |
| jooq-user-min-json.js                | 129947   | 1299.333884/s | NO   | 3       |                        |
| jooq-user-min-json.js                | 130204   | 1301.913368/s | YES  | 3       |                        |
| jooq-user-slice.js                   | 95819    | 957.964119/s  | NO   | 1       | JSON in pg             |
| jooq-user-slice.js                   | 97551    | 975.392012/s  | YES  | 1       | JSON in pg             |
| jooq-user-slice-min-json.js          | 174409   | 1743.910773/s | NO   | 2       |                        |
| jooq-user-slice-min-json.js          | 178108   | 1780.962465/s | YES  | 2       |                        |
| simple-message.js                    | 210406   | 2103.893473/s | NO   | 2       |                        |
| simple-message.js                    | 209146   | 2091.308727/s | YES  | 2       |                        |
| simple-message-with-work.js          | 16563    | 165.425161/s  | NO   | 2       | No CPU                 |
| simple-message-with-work.js          | 16520    | 165.034789/s  | YES  | 2       | No CPU                 |

### Другие языки / фреймворки аналог jooq-user-slice-min-json.js

| Language/Framework/    | Requests | Rps           | QUERIES  | REASON                | BETTER IN                                   |
|------------------------|----------|---------------|----------|-----------------------|---------------------------------------------|
| Java/Jooby/Jooq/Hikari | 214747   | 2147.316961/s | 2        | no proxy / reflection | smaller heap size                           |
| Go/Fiber/Sqlx/Pgx      | 191420   | 1914.05203/s  | 2        | native binary         | low memory ~10Mb, small artefact size ~15Mb |
