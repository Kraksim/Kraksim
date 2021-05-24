FROM openjdk:15-alpine

COPY build/libs/kraksim.jar /usr/src/kraksim.jar

CMD java -agentlib:jdwp=transport=dt_socket,address=*:8081,server=y,suspend=n -jar usr/src/kraksim.jar pl.edu.agh.cs.kraksim
