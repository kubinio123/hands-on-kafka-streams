### Read the blog

This code was created for the purpose of [this blog post](https://softwaremill.com/hands-on-kafka-streams-in-scala/), I encourage you to read it first :)

### Running the example

I assume that `sbt` is installed directly on your OS.

Defined `docker-compose` starts up a _zookeeper_, _kafka_ and _schema-registry_ instance as well as
_tools_ container with [confluent-cli-tools](https://docs.confluent.io/platform/current/installation/cli-reference.html) installed.
Scripts from `kafka-cli-scripts` directory are mounted to the _tools_ container.

#### Running kafka

Run `start-kafka.sh` script. It will start up docker network and create topics as defined in `kafka-cli-scripts/create-topics.sh`.

#### Running apps

Directory contains 4 sbt projects each being independent app:
* avro - generates and registers _Avro_ schemas for data used as keys/values in created topics
* car-data-producer - produces random data to kafka topics
* driver-notifier - kafka streams application which aggregates data from several topics, processes and produces to `driver-notifications`
* car-data-consumer - can consume data from any of created kafka topics (`driver-notifications` by default)

Please also add this two entries to your `etc/hosts`, since apps above reach kafka using docker hostnames:
```
# streams-app host entries
127.0.0.1 kafka
127.0.0.1 schema-registry
```

With `sbt` each app can be started in separate shell instance with the following command:
`sbt "project <name>" "run"`, for an ex. `sbt "project carDataProducer" "run"`.

Please run `avro` and `carDataProducer` first.

If you are _Intellij_ user then most convenient option will be to use IDE for running apps, since you can easily tweak, debug and experiment with the code.

#### Cleanup

Run `stop-kafka.sh` script.
