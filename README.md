#I. Requirement

The system will be a simple web application exposing REST API acting as a generator of the sentences from the words inserted to it by rules described below. You can input any word to the system but you need to specify its part of speech - NOUN or VERB or OBJECTIVE.

The system must be able to generate a sentence from the words according to the rule that sentence is in the form of NOUN VERB ADJECTIVE. Further description of resources / functionality is available in the next part - API proposal.

Suggestion: - Use Spring Boot or Dropwizard but it’s not mandatory.

            - No persistence is needed -  persistent storage is only a bonus.
            
            - Use JSON format for API and library https://github.com/lukas-krecan/JsonUnit in unit tests and https://github.com/jadler-mocking/jadler for integration tests. 

Note: Since this service doesn't make any call to another one so jadler library was unused and i use MockMvc of Spring boot test to develop integration tests

#II. How to run
Make sure that the network is available then we follow steps as below : 

  1. In generator-application folder, open terminal from this folder

  2. Run "docker-compose build". It will take around 5 minutes to get this step done. 
       Tasks will be perform in this steps such as create database, execute unit tests/integration tests before jar file is generated

  3. Finally, run "docker-compose up" for starting application at port 8086

At this stage, you can use browser or postman to make a request to the application

#III. API Endpoint Definitions

  1. Sentence APIs
        -  
  2. Word APIs





 

   