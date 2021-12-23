package io.github.mixren.accumulatingerrorcodecstest

import cats.effect.Async
import cats.implicits._
import org.http4s.dsl.Http4sDsl
import org.http4s.{HttpRoutes, InvalidMessageBodyFailure}


object Routes {
  def codecTestRoute[F[_] : Async]: HttpRoutes[F] = {

    val codecTester = new CodecTester[F]
    val dsl = new Http4sDsl[F] {}
    import dsl._

    HttpRoutes.of[F] {
      // Call: curl http://localhost:8080/codec/test -d '{"name": "John", "password": "123456"}' -H "Content-Type: application/json"
      case req@POST -> Root / "codec" / "test" =>
        req.as[AuthPair].flatMap(Ok(_)).handleErrorWith {
          case f: InvalidMessageBodyFailure => BadRequest(f.getMessage + "\n" + f.getCause.getMessage)
          case o => BadRequest(o.getMessage)
        }

      case GET -> Root / "codec" / "test" / name / pass =>
        codecTester.resp2(AuthName(name), AuthPassword(pass))
    }
  }
}
