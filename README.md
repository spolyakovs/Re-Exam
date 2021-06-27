# Re-Exam
Все задания сдаются как пулл-реквест к этому репозиторию.

Дедлайн 01/09/21 00:01, после чего решения не принимаются.
## SpringChat with Rooms
Задание выполняется на Java с использованием Spring. 
Требуется реализовать приложение чата.
 1. Пользователи:
    1. Заходят в приложение через Google или любой другой сервис сторонней аунтефикации
    2. Имееют возможность настроить публичный ник (имя, которое будут видеть другие пользователи)
    3. Видят список публичных и приватных комнат, могут создать комнату, зайти в существующую
 2. Комнаты: 
    1. Пользователь, создавший комнату может изменить имя комнаты и добавить новых людей по их нику
    2. Пользователь может сделать комнату приватной, в таком случае она будет отображаться в списке комнат только у тех, кто находится в этой комнате
    3. Все пользователи комнаты могут писать в ней сообщения в чате
 3. Чат в комнате:
    1. У сообщений отображается время отправки, имя пользователя, отправившего сообщение и текст сообщения
    2. Каждое новое сообщение появляется на странице у пользователя без обновления страницы и любых других действий

Все данные должны хранится в базе данных.

Так же ваше приложение должно поддерживать API:
|description|method|url|body|response|
|--|--|--|--|--|
|gets all messages from specific user in a room|GET|/$roomId/messages/$username|  |{messages: ["message 1", "message 2", ...]}|
|top 10 chatters by messages count in room|GET|/$roomId/stats/top| |{top: [{username: "user", count: 239}, ...]}|
|gets all current messages, filtered by date (date filter is optional) in a room|GET|/$roomId/all-messages|{from:?$date, to:?$date}|{messages: [{id: id, username: "user", message: "message"}, ...]}|

Приложение требуется выложить на любой публичный сервис (рекомендую Heroku), в пулреквесте к репозиторию нужно приложить весь код приложения, запись демо работы приложения и ссылку на публичный доступ к вашему приложению.

## Проверка задания будет проведена после дедлайна, удачи!
