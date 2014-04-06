FileValidator
=============
<br></br> 

FileValidator is an application used for uploading a file, validating its content and storing validation results to disk and database using Spring, Hibernate and PostgreSQL.


##Description
<p></p> 

The purpose of this application is the validation of the uploaded file whose content must be specifically structured. 
Each line in the file that is validated must have the following structure:
```
type=<VALUE_TYPE>;value=<VALUE>;
```
where the ```<VALUE_TYPE>``` defines the type of the data specified as ```<VALUE>```. There are two possible values for the  ```<VALUE_TYPE>```:  
* TEXT  
* NUMBER

Every valid value must conform to the rules of the data type that is assigned to it, as it is shown in the following table:  

| VALUE TYPE    | VALID VALUES     |
| ------------- | -----------------|
| TEXT          | All [Unicode][1] characters, except the [non-printable][2] ones  |
| NUMBER        | Decimal number   |
<p></p> 

The result of the file content vaildation for each line has the following form:
```
type=<VALUE_TYPE>;value=<VALUE>;validation_result=<VALIDATION_RESULT>
```
As the result of the value validation can be that the value is 'invalid' or 'valid' for the given type, the possible values for the ```<VALIDATION_RESULT>``` are:
* VALID 
* INVALID  

###Example
<p></p>

* File to validate:  
```
type=TEXT;value=This is an example of a text value.;  
type=TEXT;value=¡²³¤€¼½¾‘’¥×Read the source, Luke;  
type=NUMBER;value=Ima li pilota u avionu?;  
type=NUMBER;value=1234;  
type=NUMBER;value=1234aaaaa;  
type=NUMBER;value=-.15411;  
type=TEXT;value=1235;  
```
* Validation result:  
```
type=TEXT;value=This is an example of a text value.;validation_result=VALID;
type=TEXT;value=¡²³¤€¼½¾‘’¥×Read the source, Luke;validation_result=VALID;
type=NUMBER;value=Ima li pilota u avionu?;validation_result=INVALID;
type=NUMBER;value=1234;validation_result=VALID;
type=NUMBER;value=1234aaaaa;validation_result=INVALID;
type=NUMBER;value=-.15411;validation_result=VALID;
type=TEXT;value=1235;validation_result=VALID; 
```

##Implementation
<p></p>

FileValidator is a web application with a simple user interface for uploading files, implemented using Spring MVC framework. The file content validation is performed by using separate value validators that are created dinamically for each value type (TEXT or NUMBER). Only files with the correct structure can be validated, so an error is displayed if a file whose content is not speciffically structured is uploaded.  
The file content and its validation results are persited to database and can be optionally stored to disk in a file named     ```ValidationResults.txt``` located in the root folder of the FileValidator project. Technologies such as Hibernate ORM library and PostgreSQL database were used for data persistence.  
Application was implemented using Eclipse IDE and unit-tested using JUnit and Mockito testing framework.  

##Installation & setup
<p></p>

In order to start the application, a few prerequisites must be met. The following must be downloaded and installed:  
1. [Java Development Kit (JDK) 7][3]  
2. [Apache Maven][4] build automation tool  
3. Database management system such as [PostgreSQL][5]  

For establishing the connection to the desired database, the ```JpaConfiguration.properties``` file, located in the ```FileValidator\src\main\resources``` folder, needs to be edited. This file must contain:
* the name of the used JDBC driver (e.g. org.postgresql.Driver), 
* the database URL (e.g. PostgreSQL URL format: jdbc:postgresql://host:port/databaseName), and
* user credentials (username and password) for connecting to the specified database.  

Jetty Maven plugin is used for running Jetty in the Maven life-cycle. This project's ```pom.xml``` file contains the jetty plugin configuration:
```
<plugin>
  <groupId>org.eclipse.jetty</groupId>
  <artifactId>jetty-maven-plugin</artifactId>
  <version>9.1.3.v20140225</version>
  <configuration>
    <stopKey>STOP</stopKey>
    <stopPort>9999</stopPort>
    <webAppConfig>
      <contextPath>/${project.artifactId}</contextPath>
    </webAppConfig>
  </configuration>
</plugin>
```
The ```stopKey``` and ```stopPort``` will be used to stop a running instance of Jetty using the goal ```jetty:stop```. For further details, please visit [Jetty Maven plugin configuration site][6]. With this configuration, application is accessible using the following URL: ```http://localhost:8080/file-validator/```.   

##Usage
<p></p>

To start the application from terminal in project's root folder, type:  
```mvn jetty:run```  
This will start the web application in "scattered" mode using the goal jetty:run.
To run the application in "packaged" mode, (package must be previously created using ```mvn package```) the goal jetty:run-war is used:  
```mvn jetty:run-war```  
As it was mentioned in the previous chapter, this web application is accessible from the following URL: ```http://localhost:8080/file-validator/```.  
To change the port (e.g. if you want to run multiple Jetty's instances in parallel) use the option -Djetty.port:  
```mvn -Djetty.port=8181 jetty:run```.   
This enables the application to be accessible from this modified URL: ```http://localhost:8181/file-validator/```.  
To stop the running instance of Jetty, type:  
```mvn -Dstop.key=STOP -Dstop.port=9999 jetty:stop```.  

###Examples
<p></p>

The main window shows an upload form. By clicking on the 'Yes' radio button, the user chooses to store the validation results to a separate file. The following image shows that the file 'FileExample.txt' was uploaded:  
![Main window](https://raw.githubusercontent.com/lzadrija/FileValidator/master/ApplicationSnapshots/MainForm.jpg)  

By clicking on the 'VALIDATE' button, the file content validation is performed (if the uploaded file has the correct structure) and the validation results are represented in a separate window:   
![Validation results](https://raw.githubusercontent.com/lzadrija/FileValidator/master/ApplicationSnapshots/ValidationResults.jpg)

If a file with an inappropriate structure was uploaded, a descriptive error message is shown:   
![Error Illegal type](https://raw.githubusercontent.com/lzadrija/FileValidator/master/ApplicationSnapshots/IllegalType.jpg)

[1]: http://en.wikipedia.org/wiki/List_of_Unicode_characters        "List of unicode characters"
[2]: http://web.itu.edu.tr/~sgunduz/courses/mikroisl/ascii.html        "List of non printable ASCII characters"
[3]: http://www.oracle.com/technetwork/java/javase/downloads/       "Java download"
[4]: http://maven.apache.org/download.cgi       "Maven download"
[5]: http://www.postgresql.org/download/        "PostgreSQL download"
[6]: http://www.eclipse.org/jetty/documentation/current/jetty-maven-plugin.html     "Jetty plugin configuration"
