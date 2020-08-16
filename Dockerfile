FROM gradle:6.6-jdk8
WORKDIR /generator-application
COPY . .
RUN gradle build
EXPOSE 8086
ENTRYPOINT ["java", "-jar", "/generator-application/build/libs/generator-application-0.0.1-SNAPSHOT.jar"]

