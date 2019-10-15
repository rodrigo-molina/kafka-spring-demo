# Kafka Spring Consumer / Producer Example

## Run Kafka from docker compose
```
$ cd  .\src\test\resources
$ docker-compose up
```

## Run App
```
$ gradle bootRun
```

## Test it

POST localhost:8090/messages 
with body
```
{
	"key": "1",
	"value": "I'm a message value!"
}
```

check application log.