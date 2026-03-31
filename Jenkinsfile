pipeline {
    agent any

    tools {
        jdk 'Java21'   
        maven 'Maven3' 
    }

    environment {
        DOCKER_HUB = 'vimalp3692'
        IMAGE_NAME = 'devops'
        // Change: Using Jenkins workspace instead of a hardcoded /var/lib/jenkins/devops
        // This is better practice so builds don't collide.
        DEPLOYMENT = 'java-app-deployment'
        CONTAINER  = 'java-app'
    }

    stages {
        stage('Checkout Source Code') {
            steps {
                // This pulls the code from the Git repo configured in the Jenkins Job
                checkout scm
                
                /* OR, if you want to hardcode the URL (not recommended but works):
                   git branch: 'main', url: 'https://github.com/your-username/your-repo.git'
                */
            }
        }

        stage('Build Java App with Maven') {
            steps {
                // No need for 'dir' if using standard checkout, as it's in the workspace root
                sh "mvn clean package -DskipTests"
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_HUB}/${IMAGE_NAME}:latest ."
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'docker-hub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                    sh "echo \$DOCKER_PASS | docker login -u \$DOCKER_USER --password-stdin"
                    sh "docker push ${DOCKER_HUB}/${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Update Kubernetes Deployment') {
            steps {
                sh """
                    kubectl rollout restart deployment/java-app-deployment
                    kubectl rollout status deployment/${DEPLOYMENT}
                """
            }
        }
    }

    post {
        success { echo "Success !!!" }
        failure { echo "Failed !!!" }
    }
}