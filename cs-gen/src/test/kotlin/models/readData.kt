package models

import kotlinx.serialization.cbor.Cbor
import models.simple.User
import java.io.File

fun main() {
    val user = Cbor().load(User.serializer(), File("/tmp/test.cbor").readBytes());
    println(user)
}