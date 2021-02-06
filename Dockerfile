FROM openjdk

WORKDIR /Stoom

COPY target/ProjetoStoom-0.0.1-SNAPSHOT.jar /Stoom/projeto-stoom.jar

ENTRYPOINT ["java", "-jar", "projeto-stoom.jar"]

