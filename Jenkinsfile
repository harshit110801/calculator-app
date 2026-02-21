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

        stage('Code Coverage') {
            steps {
                dir('calculator-app') {
                    sh 'mvn jacoco:report'
                }
            }
        }
    }

    post {
        always {
            dir('calculator-app') {
                junit 'target/surefire-reports/*.xml'
                jacoco(
                    execPattern: 'target/jacoco.exec',
                    classPattern: 'target/classes',
                    sourcePattern: 'src/main/java'
                )
            }
        }
        failure {
            mail to: 'harshit110801@gmail.com',
                 subject: "❌ Build Failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """
Build ${env.BUILD_NUMBER} of job '${env.JOB_NAME}' has FAILED.

Check the console output here:
${env.BUILD_URL}

--
Jenkins CI
"""
        }
        success {
            mail to: 'harshit110801@gmail.com',
                 subject: "✅ Build Passed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                 body: """
Build ${env.BUILD_NUMBER} of job '${env.JOB_NAME}' has SUCCEEDED.

Check the build here:
${env.BUILD_URL}

--
Jenkins CI
"""
        }
    }
}
