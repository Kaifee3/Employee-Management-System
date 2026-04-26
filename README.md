# Employee Management System

## How to Run

1. **Clone or Download the Project**
   - Download or clone this repository to your local machine.

2. **Configure the Database**
   - Open the `Mysql.java` file located at `src/main/java/org/example/Mysql.java`.
   - Replace the placeholder database name with your actual MySQL database name in the connection string. For example:
     
     ```java
     String url = "jdbc:mysql://localhost:3306/YOUR_DATABASE_NAME";
     ```
   - Save the file after making changes.

3. **Build the Project**
   - Open a terminal in the project root directory.
   - Run the following command to build the project using Maven:
     
     ```sh
     mvn clean install
     ```

4. **Run the Application**
   - After a successful build, run the application with:
     
     ```sh
     mvn exec:java -Dexec.mainClass="org.example.Main"
     ```

## Notes
- Ensure you have Java and Maven installed on your system.
- Make sure your MySQL server is running and accessible.
- Update your database credentials in `Mysql.java` if needed (username, password, etc.).
