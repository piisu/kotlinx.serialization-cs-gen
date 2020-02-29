@file:UseSerializers(DateSerializer::class)
package models.simple



import jp.co.piisu.serialization.CsModelGen
import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.serializersModule
import serialization.jp.co.piisu.serialization.DateSerializer
import java.io.File
import java.util.*

@Serializable
data class User(val id:Int, val name:String, val likeUsers:List<User>)

@Serializable
data class Users(@SerialName("v") val users:Array<User>)


@Serializable
data class ItemInfo(val id:Int, val name:String, val created:Date)

interface IMessage {
    val id:Int
}

@Serializable
data class StringMessage(override val id: Int = 0, val message:String) :IMessage

@SerialName("IntM")
@Serializable
data class IntMessage(override val id: Int = 0, val message:Int):IMessage

@Serializable
data class Messages(val messages:List<IMessage>)


val testModule = SerializersModule {
    polymorphic(IMessage::class) {
        StringMessage::class with StringMessage.serializer()
        IntMessage::class with IntMessage.serializer()
    }
}

@ExperimentalStdlibApi
@InternalSerializationApi
fun main() {


    val modelGen = CsModelGen(testModule, dstDir = File("CborTest/CborTest/generated"))
//    modelGen.generate(User.serializer())
//    modelGen.generate(Users.serializer())
//    modelGen.generate(ItemInfo.serializer())




    modelGen.generatePolymorphic()
//
    modelGen.generate(StringMessage.serializer())
//    modelGen.generate(IntMessage.serializer())
//    modelGen.generate(Messages.serializer())


}

