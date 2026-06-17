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
                // Using 'bat' because your local Jenkins server is running on Windows
                bat 'mvn clean'
            }
        }
        
        stage('Execute Regression Suite') {
            steps {
                // Triggers the exact XML suite we built
                bat 'mvn test -DsuiteXmlFile=regression-suite.xml'
            }
        }
    }
    
    post {
        always {
            // This safely extracts your Dark Theme Extent Report so you can view it directly in Jenkins
            archiveArtifacts artifacts: 'test-output/AutomationReport.html', allowEmptyArchive: true
        }
    }
}