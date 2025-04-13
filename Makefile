# ========== Config ==========
APP_NAME=currency-converter-app
VERSION=2025.04.01
JAR_FILE=target/$(APP_NAME)-$(VERSION).jar

# ========== Targets ==========

# Clean and build the project
build:
	mvn clean install

# Run static code analysis using Checkstyle
lint:
	mvn checkstyle:check

# Run unit tests
test:
	mvn test

# Generate test coverage report with JaCoCo
coverage:
	mvn clean verify jacoco:report

# Run the Spring Boot app using Spring Boot plugin
run:
	mvn spring-boot:run

# Package into a JAR
package:
	mvn package

# Run the built JAR
jar:
	java -jar $(JAR_FILE)