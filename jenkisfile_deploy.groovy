pipeline{
    agent any
   parameters {
    string(name: 'BRANCH_NAME', defaultValue: 'master', description: 'From which branch artifacts want to deploy?')
    string(name: 'BUILD_NUM', defaultValue: '', description: 'From which build number artifacts want to deploy?')
    string(name: 'SERVER_IP', defaultValue: '', description: 'To  which want to deploy?')

  }
    stages{
        stage("download artifact"){
            steps {
                println "Here I'm downloading artifacts from S3"
                sh """
                        aws s3 ls
                        aws s3 ls s3://sivabandela
                        aws s3 ls s3://sivabandela/${BRANCH_NAME}/${BUILD_NUM}/
                        aws s3 cp s3://sivabandela/${BRANCH_NAME}/${BUILD_NUM}/hello-${BUILD_NUM}.war .
 
                   """
                
            }
        }
        stage("copy artifacts") {
            steps {
               // println "Here I'm coping artifact from Jenkins to Tomcat servers"
                //sh "ssh -i /tmp/sivalakshmanna07.pem ec2-user@${SERVER_IP} \"systemctl status tomcat\""
                //sh "scp -i /tmp/sivalakshmanna07.pem hello-${BUILD_NUM}.war  ec2-user@${SERVER_IP}:/var/lib/tomcat/webapps"
                sh '''
                    aws s3 cp s3://sivabandela/${BRANCH}/${BUILD}/hello-${BUILD}.war .
                    ls -l
                    IFS=',' read -ra ADDR <<< "${SERVERIPS}"
                    for ip in \"${ADDR[@]}\"; 
                    do
                    echo $ip
                    echo "Here we can use scp command"
                    scp -o stricthostkeychecking=no -i /tmp/sivalakshmanna07.pem hello-${BUILD}.war ec2-user@$ip:/var/lib/tomcat/webapps
                    ssh -o stricthostkeychecking=no -i /tmp/sivalakshmanna07.pem ec2-user@$ip "hostname"
                     # process "$i"
                    done
                   
            }
        }
    }
}
