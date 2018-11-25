#!/usr/bin/env bash

# first create ClusterWorkRouters cluster
sbt "runMain agh.reactive.routers_demo.ClusterNodeApp seed-node1" &
sbt "runMain agh.reactive.routers_demo.ClusterNodeApp seed-node2" &
sbt "runMain agh.reactive.routers_demo.ClusterNodeApp" & #just the node on random port

# cluster at this point should be up and running

# starting http servers which will also create routers with workers deployed on previously configured cluster
sbt "runMain agh.reactive.routers_demo.WorkHttpClusterApp 9001" &
sbt "runMain agh.reactive.routers_demo.WorkHttpClusterApp 9002" &
sbt "runMain agh.reactive.routers_demo.WorkHttpClusterApp 9003" &


# start gatling tests
sbt gatling-it:test
sbt gatling-it:lastReport