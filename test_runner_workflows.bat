call mvn clean test -Dtest=GenericWorkflowTest -Dsite.profile=paraBank -Dworkflow.name=login > para_workflow_out.txt 2>&1
call mvn clean test -Dtest=GenericWorkflowTest -Dsite.profile=globalSqaBank -Dworkflow.name=login > global_workflow_out.txt 2>&1
