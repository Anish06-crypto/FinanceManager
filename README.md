# FinanceManager 💰

A console-based Java application for managing personal finance — built using clean architecture principles.

## Features

- Create/delete accounts
- Record income/expense transactions
- List, filter, and summarize transactions
- Schedule reminders using `ScheduledExecutorService`
- Import/export `.csv` files
- Full test coverage with JUnit + Mockito
- CI/CD via GitHub Actions

## Project Structure

com.finance 

├── domain # Entities: Account, Transaction

├── service # Business logic layer 

├── repository # File-based persistence 

├── strategy # Income/Expense logic 

├── cli # Command line interface 

## To Run

mvn clean compile java -cp target/classes com.finance.Main

## Test

mvn test