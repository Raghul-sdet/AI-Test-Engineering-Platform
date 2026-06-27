pipeline {
    agent any
    
    tools {
        maven 'Maven3' 
        jdk 'jdk-17.0.12'   
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
            }
        }
        
        stage('Cleanup Previous Docker Environment') {
            steps {
                // Ensure no stale containers or volume locks from previous runs
                bat 'docker compose down --volumes --remove-orphans || true'
            }
        }
        
        stage('Build Docker Image') {
            steps {
                // Build the containerized image from your Dockerfile
                bat 'docker build -t enterprise-framework .'
            }
        }
        
        stage('Execute Dockerized Test Suite') {
            steps {
                // --abort-on-container-exit: Jenkins waits here, failing the stage if tests fail
                bat 'docker compose up --abort-on-container-exit'
            }
        }
    }
    
    post {
        always {
            // Stage: Archive Artifacts (Collect Excel report and any legacy HTML reports)
            archiveArtifacts artifacts: 'target/reports/*.xlsx, test-output/AutomationReport.html', allowEmptyArchive: true
            
            // Stage: Publish Test Results (Display TestNG trends in Jenkins UI)
            testng reportFilenamePattern: '**/testng-results.xml'
            
            // Stage: Docker Cleanup (Always reclaim disk space)
            bat 'docker compose down --volumes'
        }
    }
}