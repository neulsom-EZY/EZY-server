node {
     stage('Clone repository') {
        checkout scm
     }

    stage('Application_Config'){
        sh '''rm -rf /var/jenkins_home/workspace/EZY-Spring-Boot/src/main/resources/application.yml'''
        sh '''cp /var/jenkins_home/ConfigDir/application.yml /var/jenkins_home/workspace/EZY-Spring-Boot/src/main/resources'''
        sh '''cp /var/jenkins_home/ConfigDir/firebase-service-account.json /var/jenkins_home/workspace/EZY-Spring-Boot/src/main/resources'''
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