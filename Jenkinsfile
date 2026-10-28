
pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                echo 'Source code retrieved from GitHub.'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t fashionhub .'
            }
        }

        stage('Deploy Container') {
            steps {
                bat 'docker rm -f fashionhub-container 2>NUL'
                bat 'docker run -d -p 8080:8080 --name fashionhub-container fashionhub'
            }
        }

        stage('Verify Deployment') {
            steps {
                bat 'docker ps'
            }
        }
    }

    post {
        success {
            echo 'FashionHub deployment completed successfully!'
        }
        failure {
            echo 'Deployment failed. Check Console Output.'
        }
    }
}
