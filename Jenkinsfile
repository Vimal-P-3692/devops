pipeline {
    agent any

    environment {
        DOCKER_HUB = 'vimalp3692'
        IMAGE_NAME = 'devops'
        REPO_DIR   = '/var/lib/jenkins/devops'  // Use Jenkins home to avoid permission issues
        DEPLOYMENT = 'java-app-deployment'      // Deployment name
        CONTAINER  = 'java-app'                 // Container name in Deployment
    }

    stages {
        stage('Build Java App with Maven') {
            steps {
                dir("${REPO_DIR}") {
                    sh "sudo mvn clean package -DskipTests"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                dir("${REPO_DIR}") {
                    sh "sudo docker build -t ${DOCKER_HUB}/${IMAGE_NAME}:latest ."
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                dir("${REPO_DIR}") {
                    sh "sudo docker push ${DOCKER_HUB}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Update Kubernetes Deployment') {
            steps {
                sh """
                    sudo kubectl set image deployment/${DEPLOYMENT} ${CONTAINER}=${DOCKER_HUB}/${IMAGE_NAME}:latest
                    sudo kubectl rollout status deployment/${DEPLOYMENT}
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