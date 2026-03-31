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
                    sh "mvn clean package"
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
                    // This securely grabs your username/password from Jenkins
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                        sh "echo \$DOCKER_PASS | sudo docker login -u \$DOCKER_USER --password-stdin"
                        sh "sudo docker push ${DOCKER_HUB}/${IMAGE_NAME}:latest"
                    }
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
            echo "Success !!!"
        }
        failure {
            echo "Failed !!!"
        }
    }
}