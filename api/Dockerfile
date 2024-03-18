FROM openjdk:11
MAINTAINER khoadamtam
COPY target/api-1.0.jar app.jar

ENV spring.datasource.password="zkU4xyjQNLCTO4EcllgmQfyOWPyeuFS1"
ENV spring.datasource.url="jdbc:postgresql://dpg-cml5bl2cn0vc739natag-a.singapore-postgres.render.com:5432/user_y053?currentSchema=user"


ENTRYPOINT ["java","-jar","app.jar"]