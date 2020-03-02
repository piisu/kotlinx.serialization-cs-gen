package models

import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.cbor.Cbor
import models.simple.*
import java.io.File

fun main() {

    val cbor = Cbor(context = module)
    var obj = cbor.load(ItemInfoMaster.serializer(), File("cs-gen/CborTest/itemMaster.cbor").readBytes())
    println(obj.items.size)
    println(obj)

}