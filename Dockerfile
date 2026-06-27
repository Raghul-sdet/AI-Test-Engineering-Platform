# STEP 1: Enterprise base image specifically designed for UI Automation
# Contains Java 17, Maven 3, and Google Chrome + Linux OS dependencies.
FROM markhobson/maven-chrome:jdk-17

# STEP 2: Set the working directory inside the container
WORKDIR /usr/src/app

# STEP 3: Dependency Caching Optimization
# Copy ONLY the pom.xml first. 
COPY pom.xml .
# Download all Maven dependencies for offline use. 
# This layer gets cached, saving massive time on future builds.
RUN mvn dependency:go-offline -B

# STEP 4: Copy the actual framework source code and ALL test suite XML files
COPY src ./src
COPY *.xml ./

# STEP 5: Define the exact command that executes when the container starts
ENTRYPOINT ["mvn", "test", "-DsuiteXmlFile=parallel-classes-suite.xml"]