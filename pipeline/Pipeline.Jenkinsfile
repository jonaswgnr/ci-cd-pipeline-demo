#!groovy

def dockerexec = "/usr/local/bin/docker"
def dockerImageName = "backend"
def webserverRoot = "/usr/local/var/www"
def frontendPackage = "frontend.tar.gz"

pipeline {
    agent any

    stages {
        stage('Cleanup Workspace') {
            steps {
                cleanWs()
            }
        }
        stage('Checkout Repository') {
            steps {
                git branch: branch, credentialsId: 'GitHub',
                        url: 'https://github.com/jonaswgnr/ci-cd-pipeline-demo.git',
                        poll: true
            }
        }
        stage('Merge Branch to Master') {
            steps {
                sh 'git merge master'
            }
        }
        stage("Test and Build") {
            parallel {
                stage("backend") {
                    stages {
                        stage('Run Tests on Backend') {
                            steps {
                                dir('backend') {
                                    sh './gradlew test'
                                    jacoco()
                                }
                            }
                        }
                        stage('Compile Backend') {
                            steps {
                                dir('backend') {
                                    sh './gradlew build'
                                }
                            }
                        }
                        stage('Build Docker Image') {
                            steps {
                                script {
                                    dir('backend') {
                                        sh dockerexec + ' build -t ' + dockerImageName + ' .'
                                    }
                                }
                            }
                        }
                    }
                }
                stage("frontend") {
                    stages {
                        stage('Install Angular Modules') {
                            steps {
                                dir('frontend') {
                                    sh 'npm install'
                                }
                            }
                        }
                        stage('Test Frontend') {
                            steps {
                                dir('frontend') {
                                    sh 'ng test --watch=false --browsers=ChromeHeadless'
                                }
                            }
                        }
                        stage('Build and Package Frontend') {
                            steps {
                                script {
                                    dir('frontend') {
                                        sh 'ng build'
                                        sh 'cd dist && tar -czf ' + frontendPackage + ' frontend/*'
                                        sh 'cp dist/' + frontendPackage + " .."
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        stage('Deployment Breakpoint') {
            steps {
                timeout(60) {
                    input message: 'Continue?',
                            parameters: [booleanParam(defaultValue: false, description: '', name: 'deploy')]
                }
            }
        }
        stage("Deploy") {
            parallel {
                stage("backend") {
                    stages {
                        stage('Deploy Frontend') {
                            steps {
                                script {
                                    if (deploy) {
                                        dir("pipeline") {
                                            sh 'ansible-playbook -i hosts deploy-frontend.yml'
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                stage("frontend") {
                    stages {
                        stage('Deploy Backend') {
                            steps {
                                script {
                                    if (deploy) {
                                        dir("pipeline") {
                                            sh 'ansible-playbook -i hosts deploy-backend.yml'
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}