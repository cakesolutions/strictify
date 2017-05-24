pipeline {
  agent {
    label 'sbt-slave'
  }

  stages {
    stage('Test') {
      steps {
        ansiColor('xterm') {
          sh "sbt clean test"
        }
      }
    }

    stage('Publish Local') {
      steps {
        ansiColor('xterm') {
          sh "sbt publishLocal"
        }
      }
    }

    // Disabled due to https://github.com/os72/protoc-jar/issues/15
    /*
    stage('Test Example Apps') {
      steps {
        dir("examples/scalapb-with-refined/"){
          ansiColor('xterm') {
            sh "sbt test"
          }
        }
      }
    }
    */

    stage('Publish') {
      steps {
        echo "todo"
      }
    }

  }
}