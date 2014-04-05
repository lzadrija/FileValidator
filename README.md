FileValidator
=============
<br></br> 

FileValidator is a simple Maven Java application used for uploading a file, validating its content and storing validation results to disk and database using technologies such as Spring, Hibernate and PostgreSQL database.


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
<center>

| VALUE TYPE    | VALID VALUES     |
| ------------- | -----------------|
| TEXT          | All [Unicode][1] characters, except the [non-printable][2] ones  |
| NUMBER        | Decimal number   |
</center>
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


[1]: http://en.wikipedia.org/wiki/List_of_Unicode_characters        "List of unicode characters"
[2]: http://web.itu.edu.tr/~sgunduz/courses/mikroisl/ascii.html        "List of non printable ASCII characters"
