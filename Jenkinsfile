pipeline {
    agent any

    tools {
        maven 'Maven'
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
                    bat 'mvn clean compile'
                }
            }
        }

        stage('Test') {
            steps {
                dir('calculator-app') {
                    bat 'mvn test'
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
