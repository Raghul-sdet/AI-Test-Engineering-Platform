pipeline {
    agent any
    
    tools {
        maven 'Maven3' 
        // Perfectly matched to your local Windows Java folder
        jdk 'jdk-17.0.12'   
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                checkout scm
            }
        }
        
        stage('Clean Workspace') {
            steps {
                // Windows-specific Maven command
                bat 'mvn.cmd clean'
            }
        }
        
        stage('Execute Regression Suite') {
            steps {
                // Windows-specific Maven command
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