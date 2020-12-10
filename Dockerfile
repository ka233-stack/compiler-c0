FROM openjdk:13
WORKDIR /app
COPY ./* /app/
RUN javac -d ./output ./main.java.c0.App.java
WORKDIR /app/output