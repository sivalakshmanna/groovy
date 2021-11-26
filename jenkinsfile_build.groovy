pipeline{
    agent any
    environment{
        BRANCH = "$(env.BRANCH_NAME)"
    }
    stages{
        stage('clone the code'){
            steps{
                println "cloning the code from other repo to my repo"
                sh "ls -l"
                sh "ls -lart ./*"
                git branch = "${BRANCH_NAME}",
                url:  "https://github.com/sivalakshmanna/boxfuse-sample-java-war-hello.git"
            }
        }
        stage("build the code"){
            steps{
                println "mvn clean package"
                sh "mvn clean package"
                sh "ls -l target/"
            }
        }
        stage("upload artifacts to s3"){
            steps{
                println "uploading artifacts from s3 bucket"
                sh "echo $BUILD_NUMBER"
                sh "aws s3 cp target/hello-${BUILD_NUMBER}.war s3://sivabandela/${BRANCH_NAME}/${BUILD_NUMBER}/"
            }
        }
    }
}