pipeline {
    agent any

    tools {
        // Requires JDK 21 and Maven configured under Global Tool Configuration in Jenkins
        jdk 'JDK-21'
        maven 'Maven-3.9'
    }

    environment {
        APP_NAME     = 'devops-ecommerce'
        IMAGE_NAME   = "devopsteam/${env.APP_NAME}"
        DOCKER_TAG   = "${env.BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo "Fetching source code from Git repository..."
                checkout scm
            }
        }

        stage('Compile') {
            steps {
                echo "Compiling Java source code..."
                sh 'mvn compile'
            }
        }

        stage('Unit & Integration Tests') {
            steps {
                echo "Executing automated JUnit 5 test suites..."
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Package Application') {
            steps {
                echo "Packaging executable Fat JAR with Spring Boot Maven Plugin..."
                sh 'mvn package -DskipTests'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                }
            }
        }

        stage('Docker Build & Tag') {
            steps {
                echo "Building Docker container image: ${IMAGE_NAME}:${DOCKER_TAG}..."
                sh "docker build -t ${IMAGE_NAME}:${DOCKER_TAG} -t ${IMAGE_NAME}:latest ."
            }
        }

        stage('Kubernetes Deployment (Dev/Stage)') {
            when {
                branch 'main'
            }
            steps {
                echo "Applying Kubernetes manifests to cluster..."
                sh 'kubectl apply -f k8s/'
                sh "kubectl set image deployment/devops-ecommerce-deployment devops-ecommerce=${IMAGE_NAME}:${DOCKER_TAG}"
                sh 'kubectl rollout status deployment/devops-ecommerce-deployment --timeout=120s'
            }
        }
    }

    post {
        success {
            echo "Pipeline succeeded! Application built, tested, and containerized."
        }
        failure {
            echo "Pipeline failed. Check build logs above for details."
        }
        always {
            cleanWs()
        }
    }
}
