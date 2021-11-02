node {
     stage('Clone repository') {
        checkout scm
     }

    stage('Application_Config'){
        sh '''rm -rf {DELETE_APPLICATION}'''
        sh '''cp {SETTING_APPLICATION} {SETTING_APPLICATION_LOCATION}'''
        sh '''cp {SETTING_FIREBASE} {SETTING_FIREBASE_LOCATION}'''
    }

     stage('Build BackEnd') {
        sh'''
        ./gradlew clean build --exclude-task test
        '''
     }

     stage('docker-compose'){
        sh '''/var/jenkins_home/workspace/EZY-Spring-Boot/docker-compose up -d'''
     }
}