FROM openjdk:8-jre-slim
COPY ./build/libs/bookshare-backend-0.0.1-SNAPSHOT.jar /usr/src/bookshare/
WORKDIR /usr/src/bookshare
EXPOSE 8080
CMD ["java", "-jar", "bookshare-backend-0.0.1-SNAPSHOT.jar"]
