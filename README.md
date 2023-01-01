# AISValidator
A tool to validate AIS type 1, 2, 3 and 5 messages through RSSI interpolation and external static info information.
# Prerequisites
- A MongoDB database installed and accessible to each of the microservices.
- A configured RabbitMQ instance, with rabbitmq_web_mqtt plugin enabled.
- Python 3 installed and added to path, python dependencies as seen in the main of the python file configured.
# Installation
- Run mvn install and mvn package to generate the jar files for running the Java microservice applications
- Run npm install to fetch the required dependencies for the web project, and npm run to run it locally
# Configuration
- See configuration of each component. Python file supports command line arguments