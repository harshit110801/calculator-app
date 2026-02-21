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
