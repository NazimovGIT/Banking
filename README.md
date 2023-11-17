# Bank Accounts

Bank Accounts - это RESTful API приложение для управления банковскими счетами. Оно предоставляет конечные точки для создания счетов, внесения и снятия средств, перевода средств между счетами и получения информации о счетах.

## Используемые технологии

- Java
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven

## Функциональность

- Создание нового банковского счета путем указания имени и PIN-кода.
- Внесение средств на счет путем указания имени счета, PIN-кода и суммы.
- Снятие средств со счета путем указания имени счета, PIN-кода и суммы.
- Перевод средств с одного счета на другой путем указания имени отправителя, PIN-кода отправителя, имени получателя и суммы.
- Получение списка всех счетов, включая их имена и балансы.

## Начало работы

### Предварительные требования

- Java 8 или выше
- База данных PostgreSQL

### Установка

1. Клонируйте репозиторий:

   ```bash
   git clone https://github.com/your-username/bank-accounts.git
   ```

2. Перейдите в директорию проекта:

   ```bash
   cd bank-accounts
   ```

3. Настройте подключение к базе данных в файле `application.properties`.

4. Соберите проект с помощью Maven:

   ```bash
   mvn clean install
   ```

5. Запустите приложение:

   ```bash
   mvn spring-boot:run
   ```

6. Приложение будет доступно по адресу `http://localhost:8080`.

## API Endpoints

### Создание счета

- **URL:** `/accounts`
- **Метод:** `POST`
- **Тело запроса:**

  ```json
  {
    "name": "John Doe",
    "pin": "1234"
  }
  ```

- **Ответ:**

  ```json
  {
    "name": "John Doe",
    "balance": 0.00
  }
  ```

### Внесение средств

- **URL:** `/accounts/deposit`
- **Метод:** `PATCH`
- **Тело запроса:**

  ```json
  {
    "name": "John Doe",
    "pin": "1234",
    "amount": 100.00
  }
  ```

- **Ответ:**

  ```json
  {
    "name": "John Doe",
    "balance": 100.00
  }
  ```

### Снятие средств

- **URL:** `/accounts/withdraw`
- **Метод:** `PATCH`
- **Тело запроса:**

  ```json
  {
    "name": "John Doe",
    "pin": "1234",
    "amount": 50.00
  }
  ```

- **Ответ:**

  ```json
  {
    "name": "John Doe",
    "balance": 50.00
  }
  ```

### Перевод средств

- **URL:** `/accounts/transfer`
- **Метод:** `PATCH`
- **Тело запроса:**

  ```json
  {
    "name": "John Doe",
    "pin": "1234",
    "nameToTransfer": "Jane Smith",
    "amount": 25.00
  }
  ```

- **Ответ:**

  ```json
  {
    "name": "John Doe",
    "balance": 25.00
  }
  ```

### Получение всех счетов

- **URL:** `/accounts`
- **Метод:** `GET`
- **Ответ:**

  ```json
  [
    {
      "name": "John Doe",
      "balance": 25.00
    },
    {
      "name": "Jane Smith",
      "balance": 75.00
    }
  ]
  ```