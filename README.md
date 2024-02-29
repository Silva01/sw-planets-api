# Star Wars Planets API

This is a basic API developed in Java 17 and Spring Boot 3 to manage information about planets from the Star Wars franchise. The application uses a MySQL database to store data.

## Environment Setup

Make sure you have the following tools installed on your machine:

- Java 17
- Spring Boot 3
- MySQL

## Database Configuration

1. Create a database in MySQL to store application data:

```sql
CREATE DATABASE starwars_planets;
```

2. Update the database connection settings in the application.properties file of the Spring Boot project.
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/starwars_planets
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### Running the Application
1. Clone the repository:
```bash
git clone https://github.com/your-username/starwars-planets-api.git
```
2. Navigate to the project directory:
```bash
cd starwars-planets-api
```
3. Run the application:
```bash
./mvnw spring-boot:run
```

The application will be available at http://localhost:8080.

### API Endpoints
* GET /planets: Returns all registered planets.

* GET /planets/{id}: Returns details of a specific planet by ID.

* POST /planets: Adds a new planet. Send a JSON in the request body.

```json
{
  "name": "Tatooine",
  "climate": "Arid",
  "terrain": "Desert"
}
```

* PUT /planets/{id}: Updates information of an existing planet by ID. Send a JSON in the request body.
    
```json
{
"name": "Tatooine",
"climate": "Arid",
"terrain": "Desert"
}
```
* DELETE /planets/{id}: Deletes a planet by ID.

## Contributing
Feel free to contribute to the development of this API. Open an issue to report problems or suggest improvements. Pull requests are welcome!

## License
This project is licensed under the MIT License - see the LICENSE file for details.