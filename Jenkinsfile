pipeline {
    agent any
    
    
    tools {
        maven 'Maven3' 
        jdk 'JDK17'
    }

    stages {
        stage('Checkout from GitHub') {
            steps {
                checkout scm
            }
        }
        
        stage('Clean Workspace') {
            steps {
                // CRITICAL FIX: Using 'mvn.cmd' instead of 'mvn' for Windows environments
                bat 'mvn.cmd clean'
            }
        }
        
        stage('Execute Regression Suite') {
            steps {
                // CRITICAL FIX: Using 'mvn.cmd' to trigger the TestNG XML
                bat 'mvn.cmd test -DsuiteXmlFile=regression-suite.xml'
            }
        }
    }
    
    post {
        always {
            // Safely extracts your Dark Theme Extent Report to the Jenkins dashboard
            archiveArtifacts artifacts: 'test-output/AutomationReport.html', allowEmptyArchive: true
        }
    }
}