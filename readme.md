# Project PayMyBuddy
* @author: Pablo Miranda
* @version: 1.0
* Date début projet : 29-3-2024 - date finalisation: 19-4-2024

## Application's purpose
PayMyBuddy is an online service to simplify transactions between friends and close ones without the burden of usual banks.

It delivers a simple platform to transfer money to your contacts at one click of distance and immediately.

## Functionalities
* Landing page to welcome users 
* Login and registration with hashing to ensure security supplied by Spring.
* Transactions history and payment at one click.
* Beneficiary management for adding new friends.
* External transfer to a bank account and reload credit for the account.
* Data persistency using a mysql database for the main operations.
* Logging for debug

## Technologies used
- SpringBoot for boostraping app, default config and logging.
- Spring Security for account management, endpoints access.
- Docker and mysql for DBMS. 
- Spring JPA for data handling and Lombok to simplify models.
- Front-end with bootstrap 5 and Thymeleaf.

### Testing
* 82% coverage for 30 unit and integration tests, 100% successful.

## Main architecture
* MVC and repository pattern.

### Entity diagram for physical data

<img src=".\src\main\resources\static\db.png"/>

### Class diagram for the application of the main layers

<img src=".\src\main\resources\static\UML.png"/>