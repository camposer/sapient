# Project sapient

This is a toy application with a simple UI (using an API) that allow users to see and add sample credit cards. It has two modules:
- Backend. Written in Java (uses Spring Boot)
- Frontend. Written in JavaScript (uses Angular 6)

Integration tests were added to the backend side. End-to-end tests should be added in a new version.

## Prerequisites

- Java 8 
- Gradle 4+ (ideally)
- NodeJS 8.9+
- Docker 18+
- Docker Compose 1.18+

NOTES: 
- You can ommit the usage of docker and start project modules directly (follow the start script as a reference)
- Build process occurs locally and not in docker containers for performance sake

## Start

In order to build and start the application automatically using docker you can just enter:

```
$ ./start.sh
```

Enter in your browser: http://localhost:4200

NOTE: The [start script](start.sh) and [docker-compose yaml](docker-compose.yml) files can be seen as a build and execute reference of the project.

## API Reference v1

### GET /v1/cards

```
$ curl -X GET http://localhost:8080/v1/cards
[
	{
		id: 1,
		number: "4900000000000086",
		holderName: "John Lennon",
		limit: 200.0,
		balance: 0.0
	}, 
	// ...
]
```

### POST /v1/cards

```
$ curl -X POST -H "Content-Type: application/json" \
	-d '{ "number": "4900000000000086", "holderName": "John Lennon", "limit": 200 }' \
	http://localhost:8080/v1/cards
```

