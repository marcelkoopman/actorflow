# akka-retries
Akka messaging retries
-----------------------

This Scala/Akka project shows a concept for retrying messages.

Instead of supervising actors and restarting them, you can also resend failed work.

In this example the ServiceActor calls a UnreliableResource, which will randomly fail.
In the onFailure callback for the Future, the orignal message is propagated back to the sender as FailedWork.
When receiving FailedWork, the caller/parent will resend it as a new message.

The main app is AkkaRetriesApp.
This will bootstrap the application by sending a start message to the Orchestrator.
Using routing the service(s) will be receiving work, which fail or succeed.

```
private class ServiceActor extends Actor with ActorLogging {

  def receive = {
    case msg: WorkMsg => {
      val theSender = sender
      val result = UnreliableResource.getReversedString(msg.str)
      result.onSuccess {
        case s => {
          theSender ! FinishedWork(s)
        }
      }

      result.onFailure {
        case f => {
          val retryRemaining = msg.retryConfig.retryCount - 1
          theSender ! FailedWork(f, WorkMsg(msg.str, RetryConfig(retryRemaining, msg.retryConfig.sleepSeconds)))
        }
      }
    }
  }


}
```  
