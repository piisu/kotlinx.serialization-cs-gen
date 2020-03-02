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
    var created:Date
}

@Serializable
open class ItemInfoBase: ItemInfo {
    override var id: Int = 0;
    override var name: String = ""
    override var saleDuration:Duration = Duration(Date(0), Date(Long.MAX_VALUE))
    override var created: Date = Date(0)

    override fun toString(): String {
        return "ItemInfoBase(id=$id, name='$name', saleDuration=$saleDuration, created=$created)"
    }
}

@Serializable
class ItemInfoFood:ItemInfoBase() {
    var value:Int = 0
}

@Serializable
class ItemInfoBath:ItemInfoBase() {
    var value:Int = 0
    var interval:Int = 0
}

interface ItemInfoEquipment:ItemInfo {
    var layer:Int;
}


@Serializable
class ItemInfoWall:ItemInfoBase(), ItemInfoEquipment {
    override var layer: Int = 0
}


val module = SerializersModule {
    polymorphic(ItemInfo::class) {
        ItemInfoBase::class with ItemInfoBase.serializer()
        ItemInfoFood::class with ItemInfoFood.serializer()
        ItemInfoBath::class with ItemInfoBath.serializer()
    }
    polymorphic (ItemInfoEquipment::class){
        ItemInfoWall::class with ItemInfoWall.serializer()
    }
}

@Serializable
data class FullModel(
        val intArray:IntArray,
        val byteArray:ByteArray
)


@Serializable
data class ItemInfoMaster(val items:List<ItemInfo>)


@ExperimentalStdlibApi
@InternalSerializationApi
fun main() {
    var modelGen = CsModelGen(context = module, dstDir = File("CborTest/CborTest/generated"))

    modelGen.generate(ItemInfoBase.serializer())
    modelGen.generate(Duration.serializer())

    modelGen.generatePolymorphic(ItemInfo::class, ItemInfoBase::class)
    modelGen.generatePolymorphic(ItemInfoEquipment::class)

    modelGen.generate(FullModel.serializer())

    modelGen.generate(ItemInfoMaster.serializer())
}

