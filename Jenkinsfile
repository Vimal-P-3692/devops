pipeline {
    agent any

    environment {
        DOCKER_HUB = 'vimalp3692'
        IMAGE_NAME = 'devops'
        REPO_DIR   = '/home/ec2-user/devops'  // Path to your cloned repo
        DEPLOYMENT = 'java-app-deployment'    // Deployment name
        CONTAINER  = 'java-app'               // Container name in Deployment
    }

    stages {
        stage('Build Java App with Maven') {
            steps {
                dir("${REPO_DIR}") {
                    sh 'mvn clean package -DskipTests'
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
                    kubectl set image deployment/${DEPLOYMENT} ${CONTAINER}=${DOCKER_HUB}/${IMAGE_NAME}:latest
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