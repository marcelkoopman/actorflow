package com.github.marcelkoopman.actorflo

import java.util.concurrent.TimeUnit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

/**
  * Created by marcel on 26-8-2016.
  */
object SlowResource {

  def doSomeThingSlow(str:String):Future[String] = {

    Future {
      TimeUnit.MILLISECONDS.sleep(10)
      if (Random.nextBoolean()) {
        throw new NullPointerException("whoops")
      }
      str.reverse
    }
  }
}
