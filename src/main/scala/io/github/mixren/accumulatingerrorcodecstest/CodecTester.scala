package io.github.mixren.accumulatingerrorcodecstest

import cats.effect.Async
import cats.implicits.{catsSyntaxApplicativeError, toFlatMapOps}
import io.circe.syntax.EncoderOps
import org.http4s._
import org.http4s.circe.jsonEncoder
import org.http4s.dsl.Http4sDsl

class CodecTester[F[_]: Async] {

  val dsl = new Http4sDsl[F] {}
  import dsl._

  def resp2(name: AuthName, pass: AuthPassword): F[Response[F]] =
    Ok(s"""{ "name":"${name.value}", "password": "${pass.value}" }""").flatMap(_.as[AuthPair])
    .flatMap(v => Ok(v.asJson))
    .handleErrorWith({
      case f: InvalidMessageBodyFailure => BadRequest(f.getMessage + "\n" + f.getCause.getMessage)
      case o => BadRequest(o.getMessage.asJson)
    })

  /*def responseWithoutHttp: F[String] =
    resp2.flatMap(a =>
      a.bodyText.compile.string.flatMap(b =>
        Async[F].pure(s"Status ${a.status}: $b")
      )
    )*/

}
