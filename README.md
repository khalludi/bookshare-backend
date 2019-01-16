# Bookshare Backend

This repository is the backend portion of a project called Bookshare. The main project is to create a website where college students can buy and sell books to each other in a Craigslist-like fashion. This project was originally implemented in Python, but it has been neglected for some time. Hence, the reimplementation.

This project is aimed specifically for students of George Mason University, but the code can be reformatted for other schools. The frontend portion of this project is being developed [here]().

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Highly, highly recommend using Intellij Idea as the IDE. This project runs on Java 8. It also depends on another project that implements a CAS Server for authentication located [here](https://git.gmu.edu/kali21/cas-server).

### Installing

Once you have Java 8 installed, go ahead and clone the project.

```
$ git clone https://github.com/khalludi/bookshare-backend.git
```

Go inside the folder and 

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

### Break down into end to end tests

There are integration tests that test each part of the service separately.

```
$ ./gradlew test
```

### And coding style tests

To be implemented.

```
To be implemented.
```

## Deployment

At the moment, there is no deployment process. I would like to host the service in a Docker image. Possibly have a separate one for the database as well.

## Built With

* [CockroachDB](https://www.cockroachlabs.com/) - Resilient SQL Database
* [Flyway](https://flywaydb.org/) - Database Versioning Tool
* [Gradle](https://gradle.org/) - Dependency Management
* [Jacoco](https://github.com/jacoco/jacoco) - Java Code Coverage tool
* [Spring](https://spring.io/) - Java Framework
* [SonarQube](https://www.sonarqube.org/) - Continuous Code Quality
* [Swagger](https://swagger.io/) - API docs
* [Travis-CI](https://travis-ci.com/) - Platform for automated CI/CD

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

See also the list of [contributors](https://github.com/khalludi/bookshare-backend/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details