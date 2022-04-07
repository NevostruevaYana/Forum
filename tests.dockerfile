FROM gradle:7.1.1 as builder
RUN mkdir -p /app/src
COPY src ./app/src
COPY build.gradle.kts ./app
COPY settings.gradle.kts ./app
WORKDIR app
ENTRYPOINT ["gradle", "test", "--no-daemon" ]
