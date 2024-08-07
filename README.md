# PolyTalker

PolyTalker is a Discord TTS bot implemented with the JDA API, Lavaplayer API, and utilizes the gTTS Python library. The bot is designed to greet users who join the channel with their nickname in any supported language they prefer.

## Add PolyTalker to Your Discord Server

To add PolyTalker to your own Discord server, simply follow the [invitation link](https://discord.com/api/oauth2/authorize?client_id=1193366461884928102&permissions=3147776&scope=bot).

## PolyTalker Commands
PolyTalker comes with the following commands:

`/ttshelp: Get help and information about available commands.`

`/getlang: Receive a private message with the available languages.`

`/setlang: Set your preferred language for PolyTalker.`

setlang example:
```
 /setlang en
```
sets the language to English. By default the language is set to ja (Japanese).


## Setting up the Environment

To set up the environment for PolyTalker, you need to create a `.env` file with the following fields:

```env
TOKEN=YOUR_DISCORD_BOT_TOKEN
DB_NAME=YOUR_MYSQL_DATABASE_NAME
DB_PORT=YOUR_MYSQL_DATABASE_PORT
DB_URL=YOUR_MYSQL_DATABASE_URL
DB_USER=YOUR_MYSQL_DATABASE_USER
DB_PASS=YOUR_MYSQL_DATABASE_PASSWORD
```

- **TOKEN:** Obtain a unique token from the [Discord Developer Portal](https://discord.com/developers/applications/).
- **DB_NAME:** Your MySQL database name.
- **DB_PORT:** The Port of your MySQL database.
- **DB_URL:** The URL of your MySQL database.
- **DB_USER:** Your MySQL database username.
- **DB_PASS:** Your MySQL database password.


PolyTalker uses MySQL through the Hibernate ORM.

## Running the Application

### Locally

You can run the application locally by using a local MySQL server. Make sure to set up the `.env` file with the correct values and run the application.

### Deployment with Docker

If you choose to deploy PolyTalker, a Dockerfile and docker-compose.yaml file are available to run the bot in an isolated environment.

1. Ensure you have Docker and docker-compose installed on your system.
2. Clone the PolyTalker repository.
3. Navigate to the project directory.
4. Create the `.env` file with the required fields mentioned above.
5. Run the following commands:

```bash
docker-compose up
```

This will build and run PolyTalker in a Docker container, allowing for easy deployment and isolation.

Feel free to contribute, report issues, or suggest improvements to the PolyTalker project!
