# TinyTasks

Welcome to _TinyTasks_, the most basic task management app in the whole wide world - no fancy UI and a
very limited set of features. Fortunately, you are here to save the day and implement some missing features or improve parts of _TinyTasks_.
.

## Dependencies
- jdk 17 or later
  - Use e.g. [https://sdkman.io/install](https://sdkman.io/install) and run

    ```bash
    sdk install java 17-open
    ```

- The right version of node (v14.15.0) and yarn
  - Use e.g. [https://github.com/nvm-sh/nvm](https://github.com/nvm-sh/nvm) and run

    ```bash
    nvm install v14.15.0
    nvm use v14.15.0
    npm install -g yarn
    yarn --version
    
    # If you're on mac, also run
    xcode-select --install
    ```

 ## IDE

Option 1. GitHub Codespaces is available out of the box with a properly configured devcontainer.

Option 2. Use the IDE of your choice
  - Wwhatever you feel comfortable coding an Angular + Spring Boot project.
  - Make sure you have the jdk 17+ and Gradle 8+ installed above available in your IDE

## Development

The application consists of a frontend and a backend. Both can be started separately. The frontend is
[Angular](https://angular.io/) based and the backend is based on [Spring Boot](https://spring.io/projects/spring-boot).

Before you can start developing you need to set up your development environment. You can find good and clear
instructions on the Angular website in the [Quickstart](https://angular.io/guide/quickstart) guide.
The backend uses H2 in-memory database, therefore it is not necessary to install any database server or run it in Docker

### Frontend

The fronted was generated with [Angular CLI](https://github.com/angular/angular-cli). Run `yarn` to install the
dependencies for the app. You can also add new dependencies via `yarn add`. Run `yarn start` for a dev server.
Navigate to `http://localhost:4200/`. The app will automatically reload if you change any of the source files.

To run the dev server without starting the backend use `yarn start-local`. The application will then store its
data in the browser's local storage instead of sending the data to the backend.

Run `yarn lint` to lint your application and `yarn test` to execute the unit tests via [Karma](https://karma-runner.github.io).

The application is tested using [Cypress](https://www.cypress.io). To execute the end-to-end tests run `yarn e2e`
or `yarn e2e-local` respectively.

### Backend

Set GRADLE_USER_HOME environment variable to point to <tinytask repository root>/.gradle in order to make use of the pre-cached dependencies.

The backend was generated with [Spring Initializr](https://start.spring.io/).
Run `./gradlew bootRun` for a dev server. The server is available under `http://localhost:8080/`.

There is an included open API swagger documentation ([/api/docs](http://localhost:8080/docs)) for existing endpoints you need to extend.

Run `./gradlew test` to execute the tests.

### Coding Challenge
There are 3 tasks included in the challenge
[TASK 1](TASK%201.md) , [TASK 2](TASK%202.md) , [TASK 3](TASK%203.md) 

- Focus on completing as many tasks as you can within available time and feel free to include a solution.MD explaining how you will implement other task that you don't find the time to complete.


- Such documentation (if provided) could also provide information on how you will handle edge-cases or optimize your solution for a production scenario, including how you will implement test cases for your solution.

