sh gradlew clean jar
mvn install:install-file -Dfile=./build/libs/elasticsearch-hadoop-5.6.0.jar -DgroupId=org.elasticsearch -DartifactId=elasticsearch-hadoop -Dversion=5.6.0 -Dpackaging=jar
