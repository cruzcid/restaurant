# Show all Restaurants open by selecting a given time and day. # 
Restaurant DB is populated by selecting a given .csv file with the corresponding format specified by the test instructions.

## Technologies Used ##

* FrontEnd:
	* Thymeleaf
	* Bootstrap
* BackeEnd:
	* Spring
		* Spring Boot
		* Spring Web
		* Spring JPA
		* Spring Devtools
	* Hibernate
* Database:
	* MySQL
* Versioning control 
	* GitHub


## Project Set UP ##

1. Clone or Download project from https://github.com/cruzcid/restaurant.git 
2. Import project as Existing maven project from Eclipse or STS.
3. Create a DB (preferably on MySQL) named restaurant_db.
4. Create a user named (preferably) hbstudent.
   Eg:  CREATE USER 'hbstudent'@'localhost' IDENTIFIED BY 'hbstudent';
5. Grant Priviliges to hbstudent (recently created user). 
	GRANT ALL PRIVILEGES ON * . * TO 'hbstudent'@'localhost';
	1. If a different username or dabase has been chosen, change the according values at application.properties file inside the project.

6. Run project (On Eclipse, Spring Tool Suite or command line) as maven install.
7. Run project as Spring Boot Application (Preferably on Spring Tool Suite)


## Happy path ##

1. Open a fresh browser and visit http://localhost:8080/ 
2. Upload a .csv files with names and schedules of restaurants (with the specified format as the instructions of the test indicates).
3. To visualize a list of restaurants please select a day and time of your election. 
4. You should be visualizing a list of Restaurants for the particular selected time and day. 


## Project Structure ##

* Description: 
	* This is an annotation driven spring boot application.
	* Database tables are modeled by Java Entities
	* A service layer do the processing of the information.
	* A controller expose the services methods
	* The view is tightly coupled to the model in the controller.

 
