@echo off
set JAVA_HOME=C:\Program Files\Java\jdk-17.0.12
set PATH=%JAVA_HOME%\bin;%PATH%
call mvn clean test -Dtest=TransferFundsApiTest > mvn-transfer.log 2>&1
