    package reactiveRoutersDemo      
    
    import akka.routing._
    import akka.actor._
    import akka.event._
    import scala.concurrent.Await
   

    import akka.util.Timeout
    import scala.concurrent.duration._
    
    import akka.event.LoggingReceive
    
    object Master{
	    case class Terminated(a: ActorRef);
    }
    
    object Worker{
      case class Work(work: String)
     
    }
    
    class Master extends Actor with ActorLogging {
     val nbOfroutees :Int=5
     
     val routees = Vector.fill(nbOfroutees) {
			    val r = context.actorOf(Props[Worker])
			    context watch r
			    ActorRefRoutee(r)
	   }
     
	   var router = {
		      Router(BroadcastRoutingLogic(), routees)
     }
     
	   def receive = LoggingReceive {
	    case w: Worker.Work =>
	    	router.route(w, sender())
	    	
	    case Terminated(a) =>
		    router = router.removeRoutee(a)
		    if(router.routees.size==0)
		      context.system.terminate
		   
     }
}

  

class Worker extends Actor with ActorLogging{
  import Worker._
  
  def action: Receive = LoggingReceive {
    case Work(a)  =>
     
      log.info(s"I got to work on $a")
      context.stop(self)  
  }

  def receive = action

}




object AuctionApp extends App {
  
   val system = ActorSystem("ReactiveRouters")
  
   val master=system.actorOf(Props(classOf[Master]), "master")
    
   master ! Worker.Work("some work") 
   
  
}
