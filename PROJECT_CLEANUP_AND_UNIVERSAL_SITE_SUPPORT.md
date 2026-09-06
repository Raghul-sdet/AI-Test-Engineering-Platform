# Project Cleanup & Universal Site Support — What Changed

This document covers two things done in this pass: (1) removing everything that
wasn't part of the original 3-phase idea, and (2) the first real step toward
Phase 3 (testing any website, not just ParaBank).

## 1. What was removed, and why

The original idea was three phases:

1. Full website testing (UI end-to-end + API regression + DB) via Selenium /
   REST-Assured / JDBC.
2. AI generates scenarios + test cases automatically, exports an Excel report
   (downloadable / emailable).
3. Works on any website — AI auto-analyzes a site's DOM to generate the
   framework's code base for that site.

Across many ChatGPT/Gemini sessions, the codebase grew to ~960 files in
`src/main` and ~200 test files — most of it (tenant management, billing/
licensing, a plugin marketplace, AWS/Azure/GCP/Kubernetes deployment engines,
a knowledge graph, a 7-agent AI swarm, OWASP/security scanners, a chat
assistant, an executive dashboard, distributed cluster execution, Jira/GitHub/
Slack/Teams integrations, multi-LLM routing/voting...) had nothing to do with
any of the 3 phases above.

**Removed** (verified zero references remain anywhere in the kept code):

- `ai.agent`, `ai.agents`, `ai.analytics`, `ai.cache`, `ai.classification`,
  `ai.client`, `ai.copilot`, `ai.dashboard`, `ai.defect`, `ai.distributed`,
  `ai.failure`, `ai.http`, `ai.integration`, `ai.llm`, `ai.metrics`,
  `ai.orchestrator`, `ai.performance`, `ai.runtime`, `ai.security`,
  `ai.validation`, `ai.config` (orphaned once `llm` was gone)
- Top-level `chat`, `cloud`, `decision`, `director`, `knowledge`, `learning`,
  `licensing`, `live`, `marketplace`, `prediction`, `security`, `tenant`,
  `websocket`
- `EnterpriseQaApplication.java` (a Spring Boot web-server entry point that
  nothing in the actual TestNG pipeline ever called — tests instantiate
  services directly) and the Spring Boot dependencies/plugin in `pom.xml`
  that only it used
- 140 test classes that existed solely to test the removed code
- Orphaned config: `application.properties` (Spring server port),
  `llm.properties` (config for the removed `ai.llm` provider factory)

**Result:** `src/main` went from 961 → 217 `.java` files, `src/test` from
200 → 61. Every remaining file's imports were cross-checked against the
actual file inventory — nothing dangling.

**Kept** (this is the real project): `ai.model`, `ai.requirement`,
`ai.service`, `ai.provider`, `ai.prompt`, `ai.generator`, `ai.analysis`,
`ai.dom`, `ai.mapper`, `ai.execution`, `ai.export`, `ai.data`, `ai.healing`,
`ai.exception`, `ai.notification` (email is directly part of Phase 2 -
"Excel report... email purposes"), `ai.exploration` (autonomous site
crawling - directly relevant to Phase 3), plus the plain Selenium/REST-Assured/
JDBC test infrastructure under `src/test` (`tests`, `api`, `hybrid`, `pages`,
`repositories`, `utils`).

## 2. Universal site support — what's actually done vs. what's still ahead

Be precise about this, because "works on any website" is genuinely two very
different-sized pieces of work.

### Done: the URL is no longer hardcoded

Previously `https://parabank.parasoft.com/...` was a literal string baked into
`BaseTest.java`, `TestClassGenerator.java`, and `EndToEndAiExecution.java`.
Now all three read the target URL from one place:

```properties
# src/test/resources/config.properties
uiBaseUrl=https://parabank.parasoft.com/parabank
```

Change that one line and:
- `BaseTest` (every plain Selenium test) navigates to the new site.
- `EndToEndAiExecution`'s DOM discovery (`DOMExtractionService`) analyzes the
  new site.
- The AI-generated Selenium code (`TestClassGenerator`) navigates to the new
  site instead of ParaBank.

### Done: `TestRegistry` / `PageObjectRegistry` no longer hardcode ParaBank's business domain

This was the deeper piece. `ExecutionMappingService` uses these two registries
to map an AI-generated scenario (by matching keywords in its text) to an
*existing* TestNG test class / Page Object class to actually run. They used to
be hand-typed, banking-specific maps -
`"transfer" -> com.enterprise.banking.tests.TransferFundsTest`,
`"login" -> com.enterprise.banking.tests.LoginTest` - meaning every time you
added a test class for a new site, you had to remember to also hand-edit these
two files, or the new class would simply never be found.

They now **scan the classpath at runtime** (`ClasspathClassScanner`) and
derive keywords automatically from each class's own name
(`KeywordDeriver`) - e.g. a class called `CheckoutFlowTest` (extending
`BaseTest`) is automatically registered under the keywords `"checkout"`,
`"flow"`, and `"checkout flow"`, with zero code changes to the registry
itself. Add a new site's test classes under the configured package and
they're picked up automatically the next time the suite runs.

Configurable without touching source, via system properties:

```bash
mvn test -DsuiteXmlFile=ai-suite.xml -Dtests.basePackage=com.acme.tests -Dpages.basePackage=com.acme.pages
```

(Defaults to `com.enterprise.banking.tests` / `com.enterprise.banking.pages`
if not set.)

**Known tradeoff, stated honestly:** this can't invent domain synonyms that
aren't in the class name. The old hand-typed map had e.g. `"payment"` and
`"beneficiary"` both pointing at `TransferFundsTest` even though neither word
appears in that class name - a human had decided those were related concepts.
Auto-derivation can't do that; it only knows the literal words in the class
name. In exchange, it works on any class, in any domain, without anyone
maintaining a list.

### Also present, but NOT yet proven in a real run: AI-generated test steps bound to real DOM elements

`SeleniumCodeGenerator` (the class `AiTestOrchestrator` Stage 6 uses to write
the AI-generated Selenium code) contains logic to bind each AI test case's
step text to a real, DOM-discovered element (via `SemanticMatcher` confidence
scoring, the same approach `ActionMappingEngine` already used) - and
`AiTestOrchestrator` calls `DOMExtractionService` against the configured
`uiBaseUrl` before generating code, via a new `TargetSiteConfig` helper.

**This is a real, wired implementation - but I have not seen it run.** I
found it in my working copy while investigating this task; comparing it
against your last successful `mvn test` log shows the `[DOM-ENGINE]
Initiating DOM analysis` line that this code unconditionally prints is
**absent** from that run's Stage 6 output. That means the version you tested
did not yet contain this DOM-binding logic - what's in this delivery is
newer and unverified. I checked it carefully (every class/method it calls
exists with a matching signature, braces balance, imports resolve across the
whole project) but that is not the same as a real compile and run. **Please
run `mvn test -DsuiteXmlFile=ai-suite.xml` and send the output before
trusting this piece.**

If it works, this is the actual answer to "auto-generate brand-new test
classes for a new site": point `uiBaseUrl` at a different site, run
`EndToEndAiExecution`, and Stage 6 will discover that site's real DOM and
bind each AI-generated step to a real element on it - a genuinely new,
site-specific test class, not a hand-written one being reused via the
registry.

### Still ahead regardless

`RegistrationTest`, `TransferFundsTest`, `OpenAccountTest`, etc. encode
ParaBank's actual business flows and assertions. Pointing `uiBaseUrl` at a
different site does not make these tests meaningful for that site - they were
written by hand against ParaBank's specific pages. Nothing in this pass
changes that; it's a separate, larger piece of work (generating new test
classes - not just registering existing ones - from what the DOM/exploration
engines discover on a new site).

**What already generalizes without any of the above:** the AI generation
pipeline itself (`AiScenarioGeneratorService` -> `AiTestCaseGeneratorService`
-> `SeleniumCodeGenerator` -> `ExecutionCoordinator`) writes and compiles
*brand new* test code from whatever the DOM engine discovers on the
configured URL - it was already domain-agnostic and doesn't touch
`TestRegistry`/`PageObjectRegistry` at all (see `EndToEndAiExecution.java`'s
Stage 6/7). The registries only matter for the separate "map a scenario onto
an *existing, hand-written* test class" pipeline
(`ExecutionMappingService`).
