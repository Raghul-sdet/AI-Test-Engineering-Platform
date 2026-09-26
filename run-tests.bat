call mvn compile -q
call mvn test-compile -q
call mvn clean test -Dtest=LoginTest > mvn-logintest.log 2>&1
call mvn clean test -Dtest=GenericWorkflowTest "-Dsite.profile=paraBank" "-Dworkflow.name=transferFunds" > mvn-workflow.log 2>&1
call mvn clean test -DsuiteXmlFile=regression-suite.xml > mvn-regression.log 2>&1
