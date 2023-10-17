# Wallet Service
### Cервис, который управляет кредитными/дебетовыми транзакциями от имени игроков

## Инструкция по запуску
1. Поднять контейнер базы данных ``` docker compose up ``` 
2. Собрать приложение: ``` mvn clean package ``` 
3. Через терминал запустить архив с приложением: ``` java -jar .\target\Wallet-Service-1.0-SNAPSHOT-jar-with-dependencies.jar ```