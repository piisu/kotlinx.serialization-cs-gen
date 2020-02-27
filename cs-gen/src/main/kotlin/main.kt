package hoge

import jp.co.piisu.serialization.CsModelGen
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
interface Message

@Serializable
class IntMessage(val value:Int):Message

@Serializable
class StringMessage(val value:String):Message

@Serializable
class StringMessages(val values:Array<Message>) {
    init {
        println("test")

    }
}

@InternalSerializationApi
fun main() {

    val module = SerializersModule {
        polymorphic(Message::class) {
            IntMessage::class with IntMessage.serializer()
        }
    }
    val modelGen = CsModelGen(module)

    modelGen.generate(IntMessage.serializer())
    modelGen.generate(StringMessages.serializer())
}