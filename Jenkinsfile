pipeline {
    agent any

    environment {
    JAVA_HOME = "/usr/lib/jvm/java-21-amazon-corretto"
    PATH = "${JAVA_HOME}/bin:${env.PATH}"
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