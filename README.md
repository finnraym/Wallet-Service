# Wallet Service
### Cервис, который управляет кредитными/дебетовыми транзакциями от имени игроков

## Технологии

- Java 17
- Spring Boot 3
- Spring Framework 6
- Spring Security
- Spring Data Jpa
- Spring AOP
- Hibernate
- Swagger UI
- База данных PostreSQL
- Миграция данных при помощи Liquibase
- JWT
- Lombok
- Mapstruct
- JUnit 5
- Testcontainers
- Aspectj
- Docker

## Инструкция по запуску
1. Поднять контейнер базы данных ``` docker compose up ``` 
2. Собрать приложение: ``` mvn clean package ``` 
3. Запустить приложение через WalletServiceApplication

## Структура базы данных

### `player`

| Колонка  | Тип           | Комментарий                                     |
|----------|---------------|-------------------------------------------------|
| id       | bigint        | Уникальный идентификатор игрока, первичный ключ |
| login    | varchar(255)  | Логин игрока                                    |
| password | varchar(255)  | Пароль игрока                                   |
| balance  | numeric(10,2) | Баланс игрока                                   |

### `transaction`

| Колонка                | Тип           | Комментарий                                          |
|------------------------|---------------|------------------------------------------------------|
| id                     | bigint        | Уникальный идентификатор транзакции, первичный ключ  |
| type                   | varchar(20)   | Имя автора                                           |
| player_id              | bigint        | id игрока, кому принадлежит транзакция, внешний ключ |
| balance_before         | numeric(10,2) | Баланс до совершения транзакции                      |
| balance_after          | numeric(10,2) | Баланс после совершения транзакции                   |
| amount                 | numeric(10,2) | Сумма транзакции                                     |
| transaction_identifier | uuid          | Уникальный идентификатор транзакции для приложения   |

## API

#### POST `/auth/registration`

Регистрация нового игрока. Данные передаются в формате JSON:

```
{
    "username": "test",
    "password": "test"
}
```

Пример ответа:

```
{
    "message": "Player with login test successfully created."
}
```

Если игрок с таким логином уже есть в базе, будет выведено следующее сообщение:

```
{
    "message": "The player with this login already exists."
}
```

#### POST `/auth/login`

Авторизация игрока в приложении. Данные передаются в формате JSON:

```
{
    "username": "test",
    "password": "test"
}
```

В ответе будет передан JWT токен для дальнейшей аутентификации игрока. Пример ответа:

```
{
    "login": "test",
    "accessToken": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJnZW5lcmFsIiwiaWF0IjoxNjk4MjI3MTYxLCJleHAiOjE2OTgyMzA3NjF9.5jexC7YryLT_ViPvqtqTtg421VaPb33vg3AlrM3BS9kz4r4sEibkX_uePj5vlmwJ",
    "refreshToken": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJnZW5lcmFsIiwiaWF0IjoxNjk4MjI3MTYxLCJleHAiOjE3MDA4MTkxNjF9.EanP2UYs2cZrnF-zuaM4e4YMuaekopbWuxbwvhEU2jpnjt9S2ErYb5AAvWahtqmh"
}
```

#### GET `/players/balance`

Возвращает текущий баланс игрока. Логин игрока передается в параметрах запроса:

```
/wallet-service/players/balance?login=test
```

Если параметр будет отсутствовать будет получено сообщение об ошибке:

```
{
    "message": "Login parameter is null!"
}
```

Для аутентификации в необходимо передать Bearer token с полученным ранее JWT токеном.

Пример ответа:

```
{
    "login": "test",
    "balance": 0.0
}
```

#### GET `/players/transactions/history`

Возвращает историю транзакций игрока. Логин игрока передается в параметрах запроса:

```
/wallet-service/players/transactions/history?login=test
```

Если параметр будет отсутствовать будет получено сообщение об ошибке:

```
{
    "message": "Login parameter is null!"
}
```

Для аутентификации в необходимо передать Bearer token с полученным ранее JWT токеном.

Пример ответа:

```
{
    "playerLogin": "test",
    "transactions": [
        {
            "type": "credit",
            "balanceBefore": 0.00,
            "balanceAfter": 1500.00,
            "amount": 1500.00,
            "transactionIdentifier": "43611fa0-93d0-422f-b9d6-1a3b0147c601"
        }
    ]
}
```

#### POST `/players/transactions/credit`

Совершает кредитную транзакцию для текущего авторизованного игрока. Данные передаются в формате JSON:

```
{
    "playerLogin": "test",
    "amount": 100
}
```

Значения валидируются. Если сумма транзакции будет отрицательной, то будет получено сообщение об ошибке.

Для аутентификации в необходимо передать Bearer token с полученным ранее JWT токеном.

Пример ответа:

```
{
    "message": "Transaction completed successfully!"
}
```

#### POST `/players/transactions/debit`

Совершает дебетовую транзакцию для текущего авторизованного игрока.
Все необходимые данные передаются также, как и для кредитной транзакции.