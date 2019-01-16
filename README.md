# Bookshare Backend

This repository is the backend portion of a project called Bookshare. The main project is to create a website where college students can buy and sell books to each other in a Craigslist-like fashion. This project was originally implemented in Python, but it has been neglected for some time. Hence, the reimplementation.

This project is aimed specifically for students of George Mason University, but the code can be reformatted for other schools. The frontend portion of this project is being developed [here](https://git.gmu.edu/kali21/bookshare-frontend).

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

Highly, highly recommend using Intellij Idea as the IDE. This project runs on Java 8. It also depends on another project that implements a CAS Server for authentication located [here](https://git.gmu.edu/kali21/cas-server).

### Installing

Once you have Java 8 installed, go ahead and clone the project.

```
$ git clone https://github.com/khalludi/bookshare-backend.git
```

For project to work, you also need to clone the CAS Server project. The CAS server require Java 11 to run.

```
$ git clone https://git.gmu.edu/kali21/cas-server
```

For the CAS server, you need to have a certificate and a keystore. Use the following commands to create the keystore and certificate.

```
$ keytool -genkey -keyalg RSA -alias tomcat -keystore tomcat.keystore -storepass changeit -validity 360 -keysize 2048
$ keytool -export -alias tomcat -file thekeystore.crt 
-keystore tomcat.keystore
```

Now, you need to include the certificate file in your JDK's `cacerts` file. Make sure to change the last part of the command to the path of your JDK.

```
$ keytool -import -alias tomcat -storepass changeit -file thekeystore.crt
 -keystore "C:\Program Files\Java\jdk1.8.0_152\jre\lib\security\cacerts"
```

Last thing before running is to change `cas-user/src/main/resources/etc/cas/config/cas.properties` to reflect your configuration. Particularly line 15 with the path to your keystore.

```
15 server.ssl.keyStore=file:/etc/cas/tomcat.keystore
```

Finally, go back to the root of the `cas-user` repo and run the following command to start the server.

```
$ ./build.sh run
```

Now, going back to this folder's repo, run the following command to go to start the REST service.

```
$ gradle bootRun
```

You should now be able to access all the endpoints at [https://localhost:9090](https://localhost:9090). The API docs are available at [https://localhost:9090/swagger-ui.html](https://localhost:9090/swagger-ui.html). 

The CAS server has one login entry, username: `casUser`, password: `Mellon`. The server is basic because the project will eventually use GMU's sophisticated CAS server in production.

## Running the tests

### Break down into end to end tests

There are integration tests that test each part of the service separately.

```
$ gradle test
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

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

See also the list of [contributors](https://github.com/khalludi/bookshare-backend/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details.