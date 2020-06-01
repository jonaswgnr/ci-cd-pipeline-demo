#!groovy

def dockerexec = "/usr/local/bin/docker"
def dockerImageName = "backend"
def webserverRoot = "/usr/local/var/www"
def frontendPackage = "archiv.tar.gz"
def deploy = false

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
                                        sh 'cd dist && tar -czf ' + frontendPackage + ' frontend'
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
                            parameters: [booleanParam(defaultValue: false, description: '', name: 'deploy')],
                            submitterParameter: 'deploy'
                }
            }
        }
        stage('Deploy Frontend') {
            steps {
                script {
                    if (deploy) {
                        dir(webserverRoot) {
                            sh 'rm -R * 2>/dev/null || true'
                        }
                        dir('frontend/dist') {
                            sh 'cp ' + frontendPackage + ' ' + webserverRoot
                        }
                        dir(webserverRoot) {
                            sh 'tar -xvzf ' + frontendPackage
                            sh 'mv frontend/* .'
                        }
                    }
                }
            }
        }
        stage('Deploy Backend') {
            steps {
                script {
                    if (deploy) {
                        sh 'docker run -d --rm -p8181:8080 ' + dockerImageName
                    }
                }
            }
        }
    }
}