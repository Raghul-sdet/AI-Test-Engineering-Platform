# AI Pipeline Wiring Fix — What Changed and Why

## The problem

Running `mvn test` only ever executed the plain Selenium banking tests
(Registration, TransferFunds, OpenAccount, ...). The ~960-file AI
requirement-analysis / scenario-generation / test-case-generation / Excel /
Allure pipeline under `src/main/java/.../ai/**` was never actually exercised,
even though 61 test classes exist in `src/test/java/.../tests/**` that call
into it (including `EndToEndAiExecution`, which runs the full pipeline
end-to-end).

Two separate bugs caused this:

### 1. No TestNG suite referenced the AI test classes

`testng.xml` and every other `*-suite.xml` in the project root only listed
the banking UI/API test classes. None of them included `EndToEndAiExecution`
or any of the 60 other AI-related test classes, so Surefire never ran them.

**Fix:** added `ai-suite.xml`, which wires in `EndToEndAiExecution` (its own
`<test>` block) plus all 60 AI component/unit tests (second `<test>` block).

### 2. Provider logic was outdated

The pipeline previously relied on a complex fallback chain involving nonexistent
mock or external providers which caused confusion and
failures.

**Fix:** The project now uses Ollama exclusively via `AiProviderFactory`. The factory 
simply performs a connectivity check to `localhost:11434` (the default Ollama endpoint).
There are no fallback mock providers or external API requirements anymore — all AI 
functionality is powered by local inference.

## How to run it

Run just the AI pipeline:
```bash
mvn test -DsuiteXmlFile=ai-suite.xml
```

Run only the full end-to-end pipeline test:
```bash
mvn test -DsuiteXmlFile=ai-suite.xml -Dtest=EndToEndAiExecution
```

After a run, check:
- `target/reports/Professional_Enterprise_Report.xlsx` — the AI-generated Excel test design doc
- `target/allure-results/` — raw Allure results (`allure serve target/allure-results`)
- `test-output/` — TestNG/Extent HTML reports

## Still not run by default

`ai-suite.xml` is separate from `testng.xml` on purpose — the AI pipeline is
slower since it relies on local inference. If you want it to run by default, 
either merge its `<test>` blocks into `testng.xml`, or change
`<suiteXmlFile>testng.xml</suiteXmlFile>` in `pom.xml` to point at
`ai-suite.xml`.
