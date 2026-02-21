pipeline {
    agent {
        docker {
            image 'maven:3.8.7-eclipse-temurin-17'
            args '-v /root/.m2:/root/.m2'
        }
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                dir('calculator-app') {
                    sh 'mvn clean compile'
                }
            }
        }
        
        stage('Test') {
            steps {
                dir('calculator-app') {
                    sh 'mvn test'
                }
            }
        }
    }
    
    post {
        always {
            dir('calculator-app') {
                junit 'target/surefire-reports/*.xml'
            }
        }
    }
}
