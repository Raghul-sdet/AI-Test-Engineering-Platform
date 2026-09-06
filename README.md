# 🚀 AI Test Engineering Platform

> Enterprise AI-powered Test Engineering Platform built with **Java 17** that automates end-to-end testing for a banking application. It features UI automation, REST API automation, Database validation, Hybrid E2E testing, Performance testing (via JMeter + WireMock), and an AI-driven test scenario/case generator powered entirely by local **Ollama** models.

![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.x-green)
![TestNG](https://img.shields.io/badge/TestNG-Framework-red)
![REST Assured](https://img.shields.io/badge/REST--Assured-API-blue)
![Maven](https://img.shields.io/badge/Maven-Build-purple)
![Ollama](https://img.shields.io/badge/AI-Ollama-black)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

# 📖 Overview

AI Test Engineering Platform is an enterprise-grade QA automation framework that combines Artificial Intelligence with traditional test automation.

Instead of manually writing test cases, the platform leverages local AI (via Ollama) to automatically analyze requirements, generate scenarios, and compile enterprise test design documents. Furthermore, the platform executes an extensive suite of automated tests.

---

# ✨ Core Features

## 1. Multi-Layer Automation
- **UI Automation**: Selenium 4 and TestNG using Page Object Model.
- **API Automation**: REST Assured with JSON payload support and response validation.
- **Database Testing**: JDBC validation against an H2 database.
- **Hybrid E2E**: End-to-end flows combining UI, API, and DB checks.
- **Performance Testing**: Lightweight load testing using JMeter integrated directly into Maven, mocked locally using WireMock to ensure fast and reliable execution without hitting live production rate limits.

## 2. Local AI-Driven Test Design
- Connects to a local **Ollama** instance (free and unlimited).
- Automatically analyzes banking requirements to generate test scenarios and test cases.
- Classifies risks and establishes requirement traceability.

## 3. Enterprise Excel Reporting
Automatically aggregates all test results and AI designs into a professional, 8-sheet Excel workbook:

1. **Statistics Dashboard**
2. **Test Design Report** (AI Generated)
3. **UI Test Results**
4. **API Test Results**
5. **Database Test Results**
6. **Hybrid E2E Test Results**
7. **AI Component Test Results**
8. **Performance Test Results**

---

# ▶ Running the Project

### Prerequisites
1. **Java 17**: Ensure your `JAVA_HOME` is pointing to JDK 17.
2. **Maven 3.9+**: Ensure `M2_HOME` is set and Maven is on your `PATH`.
3. **Ollama**: Download and install [Ollama](https://ollama.com/), and pull the required model:
   ```bash
   ollama pull llama3
   ```

### Execution

Clone the repository:
```bash
git clone https://github.com/Raghul-sdet/AI-Test-Engineering-Platform.git
cd AI-Test-Engineering-Platform
```

Run the full standard QA suite (UI, API, DB, Hybrid, AI):
```bash
mvn clean test -DsuiteXmlFile=full-suite.xml
```

Run the full suite **including Performance Load Tests** (JMeter & WireMock):
```bash
mvn clean test -DsuiteXmlFile=full-suite.xml -Pperformance
```

*(Note: The AI pipeline steps run locally with Ollama and can take several minutes depending on your hardware.)*

---

# 📊 Current Status & Roadmap

### Fully Verified & Working
- Core testing phases: UI, API, DB, Hybrid E2E, and AI components.
- Local performance testing using WireMock stubs + JMeter.
- 8-sheet Excel enterprise reporting generation.

### Work in Progress (Not Yet Verified)
- **Cross-Browser Testing**: The `cross-browser-suite.xml` and Grid definitions exist, but require a running Docker Desktop daemon, which has not been fully verified in the current automated environment.
- **CI/CD Pipeline**: GitHub Actions workflows (`maven-ci.yml`) exist but have not been confirmed completely green in a remote GitHub environment yet.

---

# 👨‍💻 Author

**Raghul L**  
QA Automation Engineer  
GitHub: https://github.com/Raghul-sdet

---

# 📄 License

MIT License

Copyright © 2026 Raghul L
