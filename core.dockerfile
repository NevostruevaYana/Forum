FROM gradle:7.1.1 as builder
RUN mkdir -p /app/src
COPY ./src ./app/src
COPY ./build.gradle.kts ./app
COPY ./settings.gradle.kts ./app
COPY ./keystore.jks ./app
WORKDIR app
RUN gradle jar --no-daemon

FROM openjdk:17-alpine
RUN mkdir -p /home/app
COPY ./keystore.jks /home/app/
COPY --from=builder /home/gradle/app/build/libs/* /home/app/
WORKDIR /home/app/
ENTRYPOINT [ "java", "-jar", "forum.jar" ]
