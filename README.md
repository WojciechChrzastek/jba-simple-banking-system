# Simple banking system

## About the project

The project is designed by [JetBrains Academy](https://www.jetbrains.com/academy/) and realised as a part of [Java Developer track](https://hyperskill.org/onboarding) using [EduTools](https://plugins.jetbrains.com/plugin/10081-edutools) plugin for [IDEA InteliJ IDE](https://www.jetbrains.com/idea/).

The aim was to create application simulating simple banking system communicating with a database.

Just after running the program the user can create an account with a random number validated by Luhn algorithm and a random 4-digit PIN. After logging in, the user has several options to choose from: checking the balance, adding income, making a transfer to another account in the database, closing the account and logging out. All user inputs and "banking" operations are checked for validity.

Program uses JDBC working with [SQLite](https://www.sqlite.org/index.html) whose name can be passed on in the CLI as an argument at application startup.

[Detailed description](https://hyperskill.org/projects/93) of the project is available on JetBrains Academy website.

### Realisation of the project was divided into 4 stages:
- Stage #1. Getting to know the basic concepts of a credit card number construction. Creating user menus.
- Stage #2. Implementation of the Luhn algorithm to generate random but valid credit card numbers.
- Stage #3. Establishing a connection to the database using JDBC. Passing arguments via CLI.
- Stage #4. Adding options available for user realised by executing SQL queries to the connected database.

### Main topics covered by this project
- working with JDBC
- SQL queries
- Gradle
- Luhn algorithm
- Set interface
- CLI parameters utilization 
___
Repositories of other projects realised by me within JetBrains Academy Java Developer track:
- [Music advisor](https://github.com/WojciechChrzastek/jba-music-advisor)
- [Flashcards](https://github.com/WojciechChrzastek/jba-flashcards)
- [Coffee machine](https://github.com/WojciechChrzastek/jba-coffee-machine)
- [Tic-Tac-Toe](https://github.com/WojciechChrzastek/jba-tic-tac-toe)
- [Simple chatty bot](https://github.com/WojciechChrzastek/jba-simple-chatty-bot)
