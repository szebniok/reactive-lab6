package agh.reactive.routers_demo
import akka.actor.typed.receptionist.{Receptionist, ServiceKey}
import akka.actor.typed.scaladsl.AskPattern.Askable
import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.actor.typed.{ActorSystem, Behavior}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives.{as, complete, entity, path, post}
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import com.typesafe.config.ConfigFactory

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn
import scala.util.Try

object RegisteredHttpWorker {
  val HttpWorkerKey: ServiceKey[HttpWorker.Command] = ServiceKey("HttpWorker")

  def apply(): Behavior[HttpWorker.Command] = Behaviors.setup { context =>
    context.system.receptionist ! Receptionist.Register(HttpWorkerKey, context.self)

    Behaviors.receive(
      (context, msg) =>
        msg match {
          case HttpWorker.Work(work, replyTo) =>
            context.log.info(s"I got to work on $work")
            replyTo ! WorkerResponse("Done")
            Behaviors.same
      }
    )
  }
}

class HttpWorkersNode {
  private val instancesPerNode = 3
  private val config           = ConfigFactory.load()

  val system = ActorSystem[Nothing](
    Behaviors.empty,
    "HttpWorkersNode",
    config.getConfig("cluster-default")
  )

  for (i <- 0 to instancesPerNode) system.systemActorOf(RegisteredHttpWorker(), s"worker$i")
  //  def apply(): Behavior[Nothing] = Behaviors.setup { context =>
//    Behaviors.same
//  }
}

object ClusterNodeApp extends App {
  private val config = ConfigFactory.load()

  val system = ActorSystem[Nothing](
    Behaviors.empty,
    "ClusterWorkRouters",
    config
      .getConfig(Try(args(0)).getOrElse("cluster-default"))
      .withFallback(config.getConfig("cluster-default"))
  )

  Await.ready(system.whenTerminated, Duration.Inf)
}

object WorkHttpClusterApp extends App {
  val workHttpServerInCluster = new WorkHttpServerInCluster()
  workHttpServerInCluster.run(args(0).toInt)
}

class WorkHttpServerInCluster() extends JsonSupport {
  private val config               = ConfigFactory.load()
  private val httpWorkersNodeCount = 10

  implicit val system = ActorSystem[Nothing](
    Behaviors.empty,
    "ClusterWorkRouters",
    config.getConfig("cluster-default")
  )

  implicit val scheduler        = system.scheduler
  implicit val executionContext = system.executionContext

  for (_ <- 0 to httpWorkersNodeCount) new HttpWorkersNode()

  val workers = system.systemActorOf(Routers.group(RegisteredHttpWorker.HttpWorkerKey), "clusterWorkerRouter")

  implicit val timeout: Timeout = 5.seconds

  def routes: Route = {
    path("work") {
      post {
        entity(as[WorkDTO]) { workDto =>
          complete {
            workers.ask(replyTo => HttpWorker.Work(workDto.work, replyTo))
          }
        }
      }
    }
  }

  def run(port: Int): Unit = {
    val bindingFuture = Http().newServerAt("localhost", port).bind(routes)
    println(s"Server now online. Please navigate to http://localhost:8080/hello\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
