@file:UseSerializers(DateSerializer::class)
package models.simple

import jp.co.piisu.serialization.CsModelGen
import kotlinx.serialization.*
import kotlinx.serialization.modules.SerialModule
import kotlinx.serialization.modules.SerializersModule
import serialization.jp.co.piisu.serialization.DateSerializer
import java.io.File
import java.util.*

@Serializable
data class Duration(var start:Date, var end:Date)

interface ItemInfo {
    var id:Int
    var name:String
    var saleDuration:Duration
}

@Serializable
open class ItemInfoBase: ItemInfo {
    override var id: Int = 0;
    override var name: String = ""
    override var saleDuration:Duration = Duration(Date(0), Date(Long.MAX_VALUE))
}

@Serializable
class ItemInfoFood:ItemInfoBase() {
    var value:Int = 0
}

@Serializable
class ItemInfoBath:ItemInfoBase() {
    var value:Int = 0
}

interface ItemInfoEquipment:ItemInfo {
    var layer:Int;
}

interface ItemInfoWall:ItemInfoEquipment {}


val module = SerializersModule {
    polymorphic(ItemInfo::class) {
        ItemInfoFood::class with ItemInfoFood.serializer()
        ItemInfoBath::class with ItemInfoBath.serializer()
    }

}

@ExperimentalStdlibApi
@InternalSerializationApi
fun main() {
    var modelGen = CsModelGen(context = module, dstDir = File("CborTest/CborTest/generated"))

    modelGen.generate(ItemInfoBase.serializer())
    modelGen.generate(Duration.serializer())
    modelGen.generatePolymorphic()
}

