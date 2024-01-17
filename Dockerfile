# Use a base image with Maven to build the project
FROM maven:3.8.4-openjdk-11 AS builder

# Set the working directory
WORKDIR /app
RUN mkdir -p target/lib

# Copy only the POM file to download dependencies
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the entire project and build it
COPY . .
RUN mvn package

# Use a lightweight base image with Java to run the application
FROM openjdk:11

# Set the working directory
WORKDIR /app

# Copy the JAR file, dependencies, and other necessary files
COPY --from=builder /app/target/Discord-TTS-Bot-1.0-SNAPSHOT.jar .
COPY --from=builder /app/target/lib /app/lib
RUN mkdir -p /app/locale
RUN mkdir -p /app/sounds

# Install Python and necessary dependencies
RUN apt-get update && \
    apt-get install -y python3 python3-pip && \
    pip3 install gtts && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*


# Add additional dependencies if needed
ADD https://repo1.maven.org/maven2/org/hibernate/hibernate-core/5.6.0.Final/hibernate-core-5.6.0.Final.jar /app/lib/

# Command to run your application
CMD ["java", "-cp", "Discord-TTS-Bot-1.0-SNAPSHOT.jar:lib/*", "org.example.main.Main"]