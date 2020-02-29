@file:UseSerializers(DateSerializer::class)
package models.simple



import jp.co.piisu.serialization.CsModelGen
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.modules.SerializersModule
import serialization.jp.co.piisu.serialization.DateSerializer
import java.io.File
import java.util.*

@Serializable
data class User(val id:Int, val name:String, val likeUsers:List<User>)

@Serializable
data class Users(@SerialName("v") val users:Array<User>)

@Serializable
data class ItemInfo(val id:Int, val name:String, val created:Date)

@ExperimentalStdlibApi
@InternalSerializationApi
fun main() {
    val modelGen = CsModelGen(dstDir = File("CborTest/CborTest/generated"))
    modelGen.generate(User.serializer())
    modelGen.generate(Users.serializer())
    modelGen.generate(ItemInfo.serializer())
}

