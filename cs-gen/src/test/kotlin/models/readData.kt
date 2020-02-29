package models

import kotlinx.serialization.cbor.Cbor
import models.simple.User
import java.io.File

fun main() {
    val user = Cbor().load(User.serializer(), File("CborTest/test.cbor").readBytes());

    println(user.id)
    println(user.name)
    user.likeUsers.forEach {
        println(it)
    }

}