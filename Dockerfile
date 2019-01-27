FROM openjdk:8-jre-slim
COPY ./build/libs/bookshare-backend-0.0.1-SNAPSHOT.jar /usr/src/bookshare/
COPY ./tomcat.keystore /etc/cas/tomcat.keystore
WORKDIR /usr/src/bookshare
EXPOSE 9090
CMD ["java", "-jar", "bookshare-backend-0.0.1-SNAPSHOT.jar"]
