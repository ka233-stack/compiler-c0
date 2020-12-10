FROM openjdk:13
WORKDIR /app
COPY build.gradle gradle settings.gradle compiler-c0.iml /app/
COPY src /app/src
RUN gradle fatjar --no-daemon
