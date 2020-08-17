#I. Requirement

The system will be a simple web application exposing REST API acting as a generator of the sentences from the words inserted to it by rules described below. You can input any word to the system but you need to specify its part of speech - NOUN or VERB or OBJECTIVE.

The system must be able to generate a sentence from the words according to the rule that sentence is in the form of NOUN VERB ADJECTIVE. Further description of resources / functionality is available in the next part - API proposal.

Suggestion: 
  1. Use Spring Boot or Dropwizard but it’s not mandatory.

  2. No persistence is needed -  persistent storage is only a bonus.
            
  3. Use JSON format for API and library https://github.com/lukas-krecan/JsonUnit in unit tests and https://github.com/jadler-mocking/jadler for integration tests. 

Note: Since this service doesn't make any call to another one so jadler library was unused and i use MockMvc of Spring boot test to develop integration tests

#II. How to run
Make sure that the network is available then we follow steps as below : 

  1. After extracting generator-application.jar, in generator-application folder, open terminal from this folder

  2. Run "docker-compose build". It will take around 5 minutes to get this step done. 
     Tasks will be perform in this steps such as create database, execute unit tests/integration tests before jar file is generated

  3. Finally, run "docker-compose up" for starting application at port 8086

At this stage, you can use browser or postman to make a request to the application

#III. APIs 

  #Sentence
  1. GET "http://<host>:8086/api/sentences/" : get all sentences
  2. POST "http://<host>:8086/api/sentences/generate" without request body : generate sentences from the words in the system following form NOUN VERB ADJECTIVE
  3. GET "http://<host>:8086/api/sentences/{id}" : get specific sentence by {id}
  4. GET "http://<host>:8086/api/sentences/{id}/yodaTalk" : convert one sentence into form ADJECTIVE NOUN VERB
  5. GET "http://<host>:8086/api/sentences/{id}/showDisplayCount" : For option 2(advance) : Not Implemented yet 
  #Word
  1. GET "http://<host>:8086/api/words/" : get all words
  2. GET "http://<host>:8086/api/words/{word}" : get specific word by {word}
  3. POST "http://<host>:8086/api/words/" with request body in json format as below : add new word and this word should be unique
  Request body :
  {
  	"word":"Test",
    "wordCategory":"NOUN"
  }
  4. PUT "http://<host>:8086/api/words/{word}" with request body in json format as below : update some properties of the given word. At this moment, just update the category.
  Request body
  {
      "wordCategory":"NOUN"
  }

  
  





 

   