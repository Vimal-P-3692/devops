pipeline {
    agent any

    tools {
        jdk 'Java21'   // The name you gave in Manage Jenkins > Tools
        maven 'Maven3' // The name you gave in Manage Jenkins > Tools
    }

    environment {
        DOCKER_HUB = 'vimalp3692'
        IMAGE_NAME = 'devops'
        REPO_DIR   = '/var/lib/jenkins/devops'
        DEPLOYMENT = 'java-app-deployment'
        CONTAINER  = 'java-app'
    }

    stages {
        stage('Build Java App with Maven') {
            steps {
                dir("${REPO_DIR}") {
                    sh "mvn clean package -DskipTests"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir("${REPO_DIR}") {
                    sh "docker build -t ${DOCKER_HUB}/${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                dir("${REPO_DIR}") {
                    sh "docker push ${DOCKER_HUB}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Update Kubernetes Deployment') {
            steps {
                sh """
                    skubectl set image deployment/${DEPLOYMENT} ${CONTAINER}=${DOCKER_HUB}/${IMAGE_NAME}:latest
                    kubectl rollout status deployment/${DEPLOYMENT}
                """
            }
        }
    }

    post {
        success {
            echo "✅ Deployment updated successfully!"
        }
        failure {
            echo "❌ Pipeline failed!"
        }
    }
}