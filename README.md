## Alfatest - тестовое задание
#### Создать сервис на Spring Boot 2 + Java, который обращается к сервису курсов валют и отображает GIF
• если курс по отношению к USD за сегодня стал выше вчерашнего, то отдаем рандомную отсюда https://giphy.com/search/rich <br />
• если ниже - отсюда https://giphy.com/search/broke <br />
• если не изменился - берем отсюда https://www.choicemarket.ru/nochange.gif
#### Ссылки на внешние REST API
• REST API курсов валют - https://docs.openexchangerates.org/ <br />
• REST API гифок - https://developers.giphy.com/docs/api#quick-start-guide <br />
#### Условия
• Запросы приходят на HTTP endpoint (должен быть написан в соответствии с rest conventions), туда передается код валюты по отношению с которой сравнивается USD <br />
• Для взаимодействия с внешними сервисами используется Feign <br />
• Все настройки (валюта по отношению к которой смотрится курс, адреса внешних сервисов и т.д.) вынесены в файл application.properties <br />
• На сервис написаны тесты (для мока внешних сервисов используется @mockbean) <br />
• Для сборки должен использоваться Gradle <br />
• Результатом выполнения должен быть репо на GitHub с инструкцией по запуску <br />
• Сборка и запуск Docker контейнера с этим сервисом <br />
#### Параметры сервиса:
Параметры запроса: http://localhost:8080/currency/JPY (где JPY - код валюты) <br />
Запуск контейнера: docker run --name alfatest -p 8080:8080 alexmaxpower/alfatest




