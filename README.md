# MicroProfile generated Application

## Liberty Kommandos

### Building

Clean

    mvn clean package

Fast

    mvn package

### Running

Default Execution

    mvn liberty:run

Alternative Execution

    java -jar target/address-validation-svc.jar

Liberty Dev Mode (test changes on the fly)

    mvn liberty:dev

## Docker

### Building

    docker build -t japu/address-validation-svc .

### Running

Start

    docker run -d --name address-validation-svc -p 9083:9080 japu/address-validation-svc

Stop & Remove

    docker stop address-validation-svc
    docker rm address-validation-svc

### Publish Image

    docker login
    docker push japu/address-validation-svc

### Misc

View current log output

    docker logs address-validation-svc

See available containers

    docker images

See running containers

    docker ps

Get IP to which the Docker container listens to

    docker inspect -f "{{.NetworkSettings.IPAddress }}" address-validation-svc

## Generated Microprofile Starter Info

### Metrics

The Metrics exports _Telemetric_ data in a uniform way of system and custom resources. Specification [here](https://microprofile.io/project/eclipse/microprofile-metrics)

The example class **MetricController** contains an example how you can measure the execution time of a request.  The index page also contains a link to the metric page (with all metric info)

### Open API

Exposes the information about your endpoints in the format of the OpenAPI v3 specification. Specification [here](https://microprofile.io/project/eclipse/microprofile-open-api)

The index page contains a link to the OpenAPI information of your endpoints.

### Open Tracing

Allow the participation in distributed tracing of your requests through various micro services. Specification [here](https://microprofile.io/project/eclipse/microprofile-opentracing)

To show this capability, you need to download [Jaeger](https://www.jaegertracing.io/download/#binaries) and run ```./jaeger-all-in-one```.
Alternatively, you can download the docker image of `all-in-one` using ```docker pull jaegertracing/all-in-one:${version}```,
followed by running the docker image. Refer to [Jaeger doc](https://www.jaegertracing.io/docs/) for more info.

Open [http://localhost:16686/](http://localhost:16686/) to see the traces. You have to invoke your demo app endpoint for any traces to show on Jaeger UI.
