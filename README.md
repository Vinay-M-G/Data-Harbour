# Data Harbour Console

A microserivce which provides an interactive way of importing, Updating and Removing bulk data directly to required database with the help of data set formats such as Excel, CSV, Text files.

## Features
* Can be deployed as an independent micro service in a cluster of microservice without having any dependency on other microservices.
* Able to update multiple databases using a single interface.
* Easy to use syntax.

## UI Preview

![UI Preview](https://github.com/Vinay-M-G/Data-Harbour/assets/102874763/596f8a4d-f808-40e9-8a37-f1b67d3465ce)


![UI Preview upload file](https://github.com/Vinay-M-G/Data-Harbour/assets/102874763/94254451-e1cf-4e5e-9963-7d26a0c8cf5c)



## Usuage and Syntax
  Data required by the application to perform necessary operation should be made available in header row.
  Header row consist of details such as operation that needs to be executed, Table Name and Column names seperated using ; 
  
  For Better understanding let's consider an example of table schema created as follows

  ```

  CREATE TABLE Persons
(
  personId VARCHAR(5) NOT NULL,
  lastName VARCHAR(50) ,
  firstName VARCHAR(50) NOT NULL,
  Address VARCHAR(150) NOT NULL,
  City VARCHAR(30) NOT NULL
);

```

### For Insertion of Data

```

$INSERT;Persons;PersonID;LastName;FirstName;Address;City
1;joe;dane;address2;city2
2;van;hustle;address3;city3

```
Here $INSERT is the operation, Persons is the Table Name on which the operation will be performed, remaining are the column names 

### For Updating Data

```

$UPDATE;Persons;PersonID[unique,default=dummy];LastName;FirstName[default=dummyFirst]
2;joe;altha
3;van;Sand
4;rough;strom

```
Columns marked with unique help in constructing WHERE clause of UPDATE statement while non unique columns will make up the SET part of the clause.
The default will help to add data if data is not provided for that particular column

### For Deletion of Data

```
$REMOVE;Persons;PersonID
1;
2;
3;

```
Columns which are provided will be utilised in constructing the WHERE clause of the DELETE statement

  







