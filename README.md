# Group Formation Tool

Group Formation Tool is an online learning management system based on Spring Boot framework and written using Java, HTML, CSS and Bootstrap.

It helps maintain relationship between Admin, Instructors, TAs and Students. Spring Security is implemented for efficient role and session management. Admin can add new subjects and assign users as Instructors. Instructors can assign TAs and enroll students using a CSV file. TAs and Instructors create survey forms having various types of fields, to be filled by students. Then the instructor defines group size and selects the matching criteria for each survey question (similar, not similar, etc.), based on that groups are formed.


# Installation

Execute all ".sql" files from "SQL" folder using command provided in "execute" file.

Set following environmnts variable to connect to database
- URL
- USER
- PASSWORD

OR do changes directly in DefaultDatabaseConfiguration.java 

## Requirements

- JDK 11.x
- Gradle 6.4.1

# Contributors

Dhruv, Sanjay, Tejas, and Jaspreet with the help of Rob Hawkey, Dr. Raghav Sampangi, and Krutarth Patel at Dalhousie University as Advance Topics in Software Development Course Assignment.

Copyright 2020
