Jтображение валидированных данных пользователя Telegram через Mini App

spring.application.name=tlgrm
server.port=8082

Environment variables:
BOT_TOKEN=ваш_токен
BOT_USERNAME=ваш_бот
WEB_APP_URL=ваш_url

Эти параметры указаны в application.properties:
telegram.bot.token=${BOT_TOKEN}
telegram.bot.username=${BOT_USERNAME}
telegram.bot.web-app-url=${WEB_APP_URL}

Консоль базы данных должна быть доступна на http://localhost:8082/h2-console
