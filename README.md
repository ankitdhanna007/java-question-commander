Problem Statement
================

Write a java command line program that gives me two options. One to ask a specific question and the other option is to add questions and their answers.


Building the project
====================
To do the full build, do: `mvn clean install`
Then, run the application using `mvn spring-boot:run`


Running the project
====================
The program exposes a shell commander which have 2 commands - **ask** and **save**. 

Example

`shell:>save 'question1 ? "option1" "option2" '`

`shell:>Question Saved`

`shell:> ask 'question1 ?'`

`shell:> option1`

`option2 `


`shell:> help` will give an overview of the commands that can be used

PS: Both the commands expect a string argument, so it must be enclosed within single quotes if argument contains multiple words

Running Tests
=============
The command `mvn clean install` will run the unit tests and integration tests.
