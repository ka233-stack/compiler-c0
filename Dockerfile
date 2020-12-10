FROM openjdk:13
COPY ./* /app/
WORKDIR /app
RUN javac -d ./output ./src/main/java/c0/App.java
WORKDIR /app/output