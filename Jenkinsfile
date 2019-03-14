pipeline {
  agent any
  stages {
    stage('Checkout') {
      steps {
        git (branch: "master",credentialsId: "bitbucket-jenkins-intgr",url: "https://bnnsz@bitbucket.org/encooked/authorization.git")
      }
    }
   stage('Build') {
     steps {
       // Run the maven build
       // do your job logic 
       sh "mvn clean package"
     }
   }
  stage('Results') {
    steps {
      //junit '**/target/surefire-reports/TEST-*.xml'
      archive 'target/*.jar'
    }
  }
  stage('Deploy') {
    steps {
      // Deploy to remote server
      sh'''
       echo "Deploying to test environment $SERVER_HOST"
       ls -R
       ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST 'mkdir -p ~/encooked/authorization'
       scp 'target/authorization-0.0.1-SNAPSHOT.jar' $SERVER_USER@$SERVER_HOST:'~/encooked/authorization/app.jar'
       scp 'authorization.service' $SERVER_USER@$SERVER_HOST:'/etc/systemd/system/authorization.service'
       ssh -tt -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST << EOF 
         systemctl daemon-reload
         if systemctl is-enabled --quiet authorization; 
         then 
           echo authorization service is already enbled
         else
           systemctl enable authorization.service
         fi
         if systemctl is-active --quiet authorization 
         then
           systemctl restart authorization  
         else
           systemctl start authorization
         fi
         exit $? 
       EOF'''
   }
  }
 }
}