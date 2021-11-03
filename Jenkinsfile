node {
     stage('Clone repository') {
        checkout scm
     }

    stage('Application_Config'){
        sh '''rm -rf ${DELETE_APPLICATION}'''
        sh '''sudo cp ${SETTING_APPLICATION} ${SETTING_APPLICATION_LOCATION}'''
        sh '''sudo cp ${SETTING_FIREBASE} ${SETTING_FIREBASE_LOCATION}'''
    }

     stage('Build BackEnd') {
        sh'''
        ./gradlew clean build --exclude-task test
        '''
     }

     stage('docker-compose'){
        sh '''docker-compose up -d'''
     }
}