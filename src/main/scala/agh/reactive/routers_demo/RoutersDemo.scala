package agh.reactive.routers_demo

import akka.actor.typed._
import akka.actor.typed.scaladsl.{Behaviors, Routers}
import akka.event.LoggingReceive
import akka.routing._

object Worker {
  case class Work(work: String)

  def apply(): Behavior[Work] =
    Behaviors.receive[Work](
      (context, msg) =>
        msg match {
          case Work(work) =>
            context.log.info(s"I got to work on $work")
            Behaviors.stopped
      }
    )
}

object Master {
  case class WorkToDistribute(work: String)

  val nbOfRoutees = 5

  def apply(): Behavior[WorkToDistribute] = Behaviors.setup { context =>
    val pool   = Routers.pool(poolSize = nbOfRoutees)(Worker())
    val router = context.spawn(pool, "worker-pool")
    context.watch(router)

    Behaviors
      .receiveMessage[WorkToDistribute] {
        case WorkToDistribute(work) =>
          router ! Worker.Work(work)
          Behaviors.same
      }
      .receiveSignal {
        case (context, Terminated(router)) =>
          context.system.terminate()
          Behaviors.stopped
      }
  }
}

object Client {
  case object Init

  def apply(): Behavior[Init.type] =
    Behaviors.receive(
      (context, msg) =>
        msg match {
          case Init =>
            val master = context.spawn(Master(), "master")
            master ! Master.WorkToDistribute("some work")
            Behaviors.same
      }
    )
}

object RoutersDemo extends App {
  val system = ActorSystem(Behaviors.empty, "ReactiveRouters")
  val client = system.systemActorOf(Client(), "client")
  client ! Client.Init
}

object SimpleRouterDemo extends App {
  val system = ActorSystem(Behaviors.empty, "ReactiveRouters")

  val pool    = Routers.pool(poolSize = 5)(Worker()).withBroadcastPredicate(_ => true)
  val workers = system.systemActorOf(pool, "broadcast-workers")

  workers ! Worker.Work("some work")
//  workers ! Worker.Work("some work 2")
}
