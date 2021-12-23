package io.github.mixren.accumulatingerrorcodecstest

import cats.effect.Concurrent
import io.circe.generic.extras.semiauto.deriveUnwrappedDecoder
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.{EntityDecoder, EntityEncoder}
import org.http4s.circe.{accumulatingJsonOf, jsonEncoderOf}


case class AuthPair(name: AuthName, password: AuthPassword)
object AuthPair {
  implicit val decoder: Decoder[AuthPair] = deriveDecoder[AuthPair]
  implicit val pairEncoder: Encoder[AuthPair] = deriveEncoder[AuthPair]
  implicit def pairDecoder[F[_]: Concurrent]: EntityDecoder[F, AuthPair] = accumulatingJsonOf[F, AuthPair]
  implicit def entityEncoder[F[_]]:           EntityEncoder[F, AuthPair] = jsonEncoderOf
}

case class AuthPassword private(value: String) extends AnyVal
object AuthPassword {
  def isValidPassword(str: String): Boolean = str.length > 5 && !str.contains(' ')

  val strError = "Invalid password value. Password should have 6 or more symbols and not contain spaces."
  implicit val decoder: Decoder[AuthPassword] = deriveUnwrappedDecoder[AuthPassword].validate(
    _.value.asString match {
      case Some(value) => isValidPassword(value)
      case None => false
    },
    strError
  )
  implicit val passwordEncoder: Encoder[AuthPassword] = deriveEncoder[AuthPassword]
}

case class AuthName private(value: String) extends AnyVal
object AuthName {
  def isValidName(str: String): Boolean = str.length > 2 && !str.contains(' ')

  val strError = "Invalid name value. Name should have 3 or more symbols and not contain spaces."
  implicit val decoder: Decoder[AuthName] = deriveUnwrappedDecoder[AuthName].validate(
    _.value.asString match {
      case Some(value) => isValidName(value)
      case None => false
    },
    strError
  )
  implicit val nameEncoder: Encoder[AuthName] = deriveEncoder[AuthName]
}
