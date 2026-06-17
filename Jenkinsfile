pipeline {
    agent any
    
    tools {
        maven 'Maven3' 
        jdk 'jdk-17.0.12'   
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                checkout scm
            }
        }
        
        // --- NEW DIAGNOSTIC STAGE ---
        stage('Diagnostics') {
            steps {
                bat 'echo %JAVA_HOME%'
                bat 'java -version'
                bat 'mvn.cmd -version'
            }
        }
        
        stage('Clean Workspace') {
            steps {
                bat 'mvn.cmd clean'
            }
        }
        
        stage('Execute Regression Suite') {
            steps {
                bat 'mvn.cmd test -DsuiteXmlFile=regression-suite.xml'
            }
        }
    }
    
    post {
        always {
            archiveArtifacts artifacts: 'test-output/AutomationReport.html', allowEmptyArchive: true
        }
    }
}