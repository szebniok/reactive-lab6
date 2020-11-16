package agh.reactive.routers_demo

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class HttpWorkerGatlingTest extends Simulation {

  val httpProtocol = http //values here are adjusted to cluster_demo.sh script
    .baseUrl("http://localhost:9001")
    //baseUrls("http://localhost:9001", "http://localhost:9002", "http://localhost:9003")
    .acceptHeader("text/plain,text/html,application/json,application/xml;")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("BasicSimulation")
    .feed(jsonFile(classOf[HttpWorkerGatlingTest].getResource("/data/work_data.json").getPath).random)
    .exec(
      http("work_basic")
        .post("/work")
        .body(StringBody("""{ "work": "${work}" }"""))
        .asJson
    )

  setUp(
    scn.inject(constantConcurrentUsers(1).during(10.seconds))
  ).protocols(httpProtocol)
}
