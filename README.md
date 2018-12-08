# Web stat
Simple web stat application with two variants: on Web flux, and regular spring boot app.

Collects stats for user and pages ids. Id - integer type.
You could save entry, or read stats. 
## How to start application:

## Pre requests
MongoDB should be installed. 
You could re-define datasource params in file application.properties
or via params then launching jar file.
  
## Server app

Run `./gradlew bootRun` to start application

## To add entry
You should post a http request like that:
curl -i --header "Content-Type: application/json"  --data '{"userId":"1","pageId":"2"}' http://localhost:8080/entries/

## To see stats: 
You must define period of statistics. i.e. 
http://localhost:8080/entries/?start=2018-11-01T07:18:54.615&finish=2018-12-03T07:18:54.615



