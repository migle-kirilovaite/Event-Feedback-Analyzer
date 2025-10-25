# Event Feedback Analyzer

An application for analyzing event feedback.
Deployed application is available on https://event-analyzer-62743443573.europe-west1.run.app/

## Prerequisites
- Docker
- Git

## System Requirements
The application runs in containers that include:
- Java 17 (Eclipse Temurin)
- Node.js 18
- Maven 3.9

Note: While you don't need to install these directly on your system (as they're included in the Docker containers), your system should be capable of running these resources within Docker.

## Running with Docker

1. Clone the repository
```bash
git clone https://github.com/migle-kirilovaite/Event-Feedback-Analyzer.git
cd Event-Feedback-Analyzer
```

2. Build the Docker image
```bash
docker build -t event-feedback-analyzer .
```

3. Run the container
```bash
docker run -p 8080:8080 event-feedback-analyzer
```

Once the container is running, you can access the application at:
`http://localhost:8080`
