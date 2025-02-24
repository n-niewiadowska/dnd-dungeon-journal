# DnD Dungeon Journal

*Dungeons and Dragons* is a role-playing game with interactive storytelling, where players gather together and take part in an imaginary adventure. It is hosted by **Dungeon Master**, who manages the whole campaign and tells the story.

This minimalistic application, *Dungeon Journal*, was created to help DM organize their campaigns and track their progress. It is meant to work on localhost, for one person who would like some assistance in their fantastic jouneys. 

*Dungeon Journal* got split into two main parts, **Backend** and **Frontend**, both created as university projects for subjects *Industrial applications* and *Frontend development II*.

#### Contents:

[1. Backend](#1-backend)

- [1.1 Basic usage](#11-basic-usage)

- [1.2 Domain and relations](#12-domain-and-relations)

- [1.3 Rest controllers](#13-rest-controllers)

- [1.4 Web controllers](#14-web-controllers)

[2. Frontend](#2-frontend)

- [2.1 Basic usage](#21-basic-usage)

- [2.2 Functionalities](#22-functionalities)

- [2.3 Internationalization](#23-internationalization)

- [2.4 Interface](#24-interface)

## 1. Backend

Server of the application is a `Gradle` project with `Spring Boot` standing on `Java 21`. It connects with `PostgreSQL` through Docker.

It also uses dependencies like:

`Lombok` (for cleaner code), `Mapstruct` (for dto-domain mapping), `Thymeleaf`, `Spring MVC` (to create frontend-like templates).

### 1.1 Basic usage

To use this application you need to have **Docker Engine** installed for database connection and an IDE of your choice to run the code.

1. Run Postgres container using shell script:

```sh
./backend/docker/run_postgres.sh
```

Feel free to change configuration (password, username etc) but remember to adjust it in `src/main/resources/application.properties` as well.

2. Run gradle script in the main `./backend` directory:

```sh
chmod +x ./gradlew
./gradlew bootRun
```

Or, if you use IntelliJ IDEA, click Run button for `BackendApplication.java`.

Database entries will be initialized and the app is ready to use at *http://localhost:8080*.

### 1.2 Domain and relations

There are 5 domain classes:

- **Campaign** - represents a whole campaign with all its data like played/planned sessions, characters taking part in the story, whether the game is planned/in progress/
finished etc.

- **DndCharacter** - represents a single character and their important information (name, age, class, race and skills)

- **MainSkill** - it is a character's main skill, has its level and special item that enhances the ability

- **MainItem** - item connected to the skill, has its own bonus

- **Session** - represents a single session that took place as a part of the campaign. It stores notes Dungeon Master made from this meeting and acts as reminder before the next one.

Every domain class is a database entity and has its own dto repository, service and API controller. They are also connected with one another with database relationships.

- **Unidirectional OneToOne**, DndCharacter -> MainSkill

One character is connected to exactly one skill and is an owner of the relationship. Only character holds a reference to the skill. The MainSkill entity does not know that it is associated with a DndCharacter.

1. Cascade type - ALL - all operations performed on the DndCharacter entity will cascade to the MainSkill entity.
2. Fetch type - EAGER - skill is loaded immediately when character is loaded.

- **Bidirectional OneToOne**, MainSkill <-> MainItem

Both entities know about each other and MainSkill is a relationship owner. One skill is associated with exactly one item.

1. Cascade type - MERGE (both sides) - when one entity is updated, the other one gets updated too
2. Fetch type - EAGER (in MainSkill) - item is loaded immediately with skill.

- **OneToMany/ManyToOne**, Campaign <-> Session

One campaign is associated with many instances of sessions, one session can be only associated with one campaign. Session owns the foreign key, but both sides can navigate to each other.

OneToMany parameters:

1. Cascade type - REMOVE - all associated sessions are removed when campaign is deleted.
2. Fetch type - LAZY - sessions are loaded only when explicitly accessed.

ManyToOne parameters:

1. Cascade type - REFRESH - reloads the state of the entity.
2. Fetch type - EAGER - campaign is loaded immediately with session, as it's an important field.


- **Unidirectional ManyToMany**, Campaign -> DndCharacter

Many characters can be associated with many campaigns and vice versa, but only campaign references the instances.

1. Cascade type - DETACH - detaches the associated entity from the persistence context.
2. Fetch type - LAZY - characters are loaded only when explicitly accessed.

### 1.3 Rest controllers

Every domain class has its own rest controller that uses DTO and services. It covers:

- CRUD operations

- User-defined queries (JPQL and native queries defined in repositories)

- adding/removing elements from the entity's lists

- special `/search` endpoint (for Campaign) with filters, sort and pagination

These controllers are also tested in Postman. Requests collections can be found in `src/test/resources/postman`.

### 1.4 Web controllers

For a dynamic display of some operations' results, **Spring MVC** and **Thymeleaf** were used. 

There are templates for CRUD for Campaign, Campaign search, displaying and deleting Characters and displaying Sessions by Campaign.

Example template for Campaign search:

![Search](/backend/src/main/resources/static/screenshot.png)

## 2. Frontend

Web application of this project was written in `TypeScript`, more precisely in `Angular 18`. It is quite simple, as it presents only a few functionalities from backend. 

Additionally, I used `ngx-translate` for internationalization and `PrimeNG 18` with `Primeicons` for reusable components.

### 2.1 Basic usage

To use this application, you need to run all the steps from [1.1 Basic usage](#11-basic-usage) first. Then you can go to `frontend/` directory.

1. Install project dependencies

```sh
npm install
```

2. Run the app

```sh
ng serve
```

It is now ready to use at *http://localhost:4200*.

### 2.2 Functionalities

This project provides you with three main functionalities, all presented in master-detail structure:

- **Manage campaigns**

It allows user to see all campaigns with a possibility to filter and sort them. Filters, sorting and pagination are written on the frontend side. From list view you can create new campaign go to details page for every element. In the details page, there are three options to manage this campaign: edit it (navigates to the form), delete (will show a modal component to confirm) or simply go back to the list.

![Campaigns](/frontend/src/assets/screenshots/campaigns.png)

- **Manage characters**

It looks exactly like campaign management, but has a bit different filters/sorting and form has a nested FormGroup. In the list view you can create new character or go to the details and in details there is a possibility to edit or delete the character.

![Characters](/frontend/src/assets/screenshots/character.png)

- **Browse skills**

It provides only list and details view, where list has filters, sorting and pagination, and details provide only *Go back* button.

![Skills](/frontend/src/assets/screenshots/skill.png)

- Other

In the menu bar at the top, user can switch the theme of the project from light to dark. It is also possible to change the language - available options are English and Polish.

![Dark theme](/frontend/src/assets/screenshots/dark.png)

### 2.3 Internationalization

App internationalization is possible thanks to `ngx-translate`. Translations are stored as JSON files in `src/assets/i18n` and loaded dynamically with HttpLoader.

Translated sentences are used in components with TranslatePipe, TranslateDirective (if inner HTML is needed) and occasionally TranslateService (for toast notifications). 

English is a default language, but it can be changed to Polish. Adding another language would be simple and require only a new JSON file (e.g. de.json) and a next element in addLangs() list in `app.component.ts`, for example:

```ts
public constructor(private translate: TranslateService) {
    this.translate.addLangs(['en', 'pl', 'de']);     // added German as a possible language
    this.translate.setDefaultLang('en');
    this.translate.use('en');
  }
```

### 2.4 Interface

Interface was designed with SCSS and PrimeNG components. Main goal was to make it clear, easy to use, responsive on both desktop and mobile and adjusted to current web standards. 

![Home - desktop](/frontend/src/assets/screenshots/home.png)

![Home - mobile](/frontend/src/assets/screenshots/mobile.png)