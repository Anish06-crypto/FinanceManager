# 💰 FinanceManager – Java Console App

A clean-architecture Java application to manage personal finance via CLI. Built with modular design, strategy patterns, and full test automation.

---

## 📦 Features

- ✅ Create/delete accounts
- ✅ Record income and expense transactions
- ✅ Automated balance calculation
- ✅ Monthly summaries & reports
- ✅ Scheduled reminders for due dates
- ✅ Import/export `.csv` files
- ✅ Configurable via `app.properties`
- ✅ Unit tested with JUnit + Mockito
- ✅ CI/CD via GitHub Actions

---

## 🧱 Architecture Overview

CLI 

└── Service Layer 

└── Domain Layer 

└── Repository Layer 

└── Strategy, Factory, Notification, Config, Utils 

└── DI Layer (AppContext)


Built with clean layering and full separation of concerns.  
[📸 View architecture diagram](Layers.png) ← (link your diagram file)

---

## 🚀 How to Run

```bash
mvn clean compile
java -cp target/classes com.finance.Main