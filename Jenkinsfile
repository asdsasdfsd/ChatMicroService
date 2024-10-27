pipeline {
    agent any

    tools {
        jdk 'jdk8'  
        maven 'Maven3'  
    }

    environment {
        DOCKERHUB_REPO_CHATMICROSERVICE = 'sh-chatmicroservice' 
        DOCKERHUB_CREDENTIALS = 'dockerhub-credentials'        // Docker Hub 凭证
        DOCKERHUB_USER = 'tigerwk'                             // Docker Hub 用户名
    }

    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/asdsasdfsd/ChatMicroService', branch: 'master'
            }
        }

        stage('Build ChatMicroService') {
            steps {                         
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build ChatMicroService Docker Image') {
            steps {
                script {
                    def chatMicroServiceImage = docker.build("${DOCKERHUB_USER}/${DOCKERHUB_REPO_CHATMICROSERVICE}:latest")   
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                script {
                    try {
                        docker.withRegistry('https://registry-1.docker.io/v2/', "${DOCKERHUB_CREDENTIALS}") {
                            echo 'Logged in to Docker Hub successfully'
                        }
                    } catch (Exception e) {
                        echo "Error during login: ${e.message}"
                        currentBuild.result = 'FAILURE'
                    }
                }
            }
        }

        stage('Push ChatMicroService Image to Docker Hub') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', "${DOCKERHUB_CREDENTIALS}") {
                        def chatMicroServiceImage = docker.image("${DOCKERHUB_USER}/${DOCKERHUB_REPO_CHATMICROSERVICE}:latest")
                        chatMicroServiceImage.push()
                    }
                }
            }
        }
    }

    post {
        always {
            script {
                sh "docker rmi ${DOCKERHUB_USER}/${DOCKERHUB_REPO_CHATMICROSERVICE}:latest || true"
            }
        }
    }
}


