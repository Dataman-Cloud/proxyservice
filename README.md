# About
This service is for customer who needs to transform "spring-cloud" based micro-services project to be staged into shurenyun paas platefrom.

Has the following branches:

* master branch for shurenyun2.0 stack.
* crane branch for shurenyun3.0 stack.


# Build and Run
	#Edit src/main/resources/application.properties for the address of "shurenyun.api" and "shurenyun.swarmmgt"
	
	#mvn package
	#java -jar ./target/proxyservice.jar
	
	

# Testing
	#copy docker-compose template file to /data/{stack_name}/docker-compose-template.yml
	#then run the following test case.
	./test/createStack.sh
	./test/searchStack.sh
	./test/delete.sh

