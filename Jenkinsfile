node {
     stage('Clone repository') {
        checkout scm
     }

    stage('Application_Config'){
        sh '''rm -rf ${DELETE_APPLICATION1}'''
        sh '''rm -rf ${DELETE_APPLICATION2}'''
        sh '''rm -rf ${DELETE_APPLICATION3}'''
        sh '''sudo cp ${SETTING_APPLICATION} ${SETTING_APPLICATION_LOCATION}'''
        sh '''sudo cp ${SETTING_FIREBASE} ${SETTING_FIREBASE_LOCATION}'''
    }

     stage('Build BackEnd') {
        sh'''
        ./gradlew clean build --exclude-task test
        '''
     }

     stage('reset'){
         sh'''docker stop ${DOCKER_APP}_1 || true'''
         sh'''docker rm ${DOCKER_APP}_1 || true'''
         sh'''docker rmi ${DOCKER_APP}:latest || true'''
         sh'''docker stop ${REDIS}_1 || true'''
         sh'''docker rm ${REDIS}_1 || true'''
      }

     stage('docker-compose'){
        sh '''./docker-compose up -d'''
     }
}