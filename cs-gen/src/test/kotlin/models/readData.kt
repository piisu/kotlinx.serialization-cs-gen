package models

import kotlinx.serialization.cbor.Cbor
import models.simple.*
import java.io.File

fun main() {

    val cbor = Cbor(context = module)
    var obj = cbor.load(ItemInfoBath.serializer(), File("CborTest/itemInfo.cbor").readBytes())


    println(obj)
    println(obj.id)
    println(obj.name)
    println(obj.saleDuration)
    println(obj.value)

}