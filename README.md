# reactive-lab6

## Local Http server with local router
To run WorkHttpApp
```bash
sbt "runMain agh.reactive.routers_demo.WorkHttpApp 9123"
```

Simple curl to test WorkHttpApp
```bash
curl -X POST \
  http://localhost:9123/work \
  -H 'Content-Type: application/json' \
  -H 'cache-control: no-cache' \
  -d '{
	"work": "clean my room"
  }'
```

## Http server with Pool router on cluster setup

See: `application.conf` file

First setup node cluster:
```bash
# first create ClusterWorkRouters cluster
sbt "runMain agh.reactive.routers_demo.ClusterNodeApp seed-node1" &
sbt "runMain agh.reactive.routers_demo.ClusterNodeApp seed-node2" &
sbt "runMain agh.reactive.routers_demo.ClusterNodeApp" & #just the node on random port
```

Then run separate server HTTP instances with configured Pool router
```bash
# starting http servers which will also create routers with workers deployed on previously configured cluster
sbt "runMain agh.reactive.routers_demo.WorkHttpClusterApp 9001" &
sbt "runMain agh.reactive.routers_demo.WorkHttpClusterApp 9002" &
sbt "runMain agh.reactive.routers_demo.WorkHttpClusterApp 9003" &
```

## Gatling performance test

(see: https://gatling.io/docs/current/quickstart/)

To start basic gatling performance test, run:
```bash
sbt gatling-it:test
```

To open last report:
```bash
sbt gatling-it:lastReport
```

# Hints

Use to be sure that you've killed every instance of sbt
```bash
ps ax | grep sbt | awk '{print $1}' | xargs kill -9
```


