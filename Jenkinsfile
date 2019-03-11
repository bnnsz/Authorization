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
       ssh -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST 'rm -f -r ~/docker/authorization; mkdir -p ~/docker/authorization'
       scp 'target/authorization-0.0.1-SNAPSHOT.jar' $SERVER_USER@$SERVER_HOST:'~/docker/authorization'
       scp 'Dockerfile' $SERVER_USER@$SERVER_HOST:'~/docker/authorization'
       ssh -tt -o StrictHostKeyChecking=no $SERVER_USER@$SERVER_HOST << EOF 
       cd ~/docker/authorization
       docker build .
       echo Delete old container...
       docker rm -f authorization
       echo "Run new container"
       mkdir -p /var/authorization_home
       docker run -v /var/authorization_home:/app -d --env DEPLOYMENT_ENV="test" --network="host" --name authorization authorization 
       exit $? 
       EOF'''
   }
  }
 }
}