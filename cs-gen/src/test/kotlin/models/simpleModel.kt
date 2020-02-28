package models.simple

import jp.co.piisu.serialization.CsModelGen
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import java.util.*

@Serializable
data class User(val id:Int, val name:String, val likeUsers:List<User>)

@Serializable
data class Users(val users:Array<User>)


@InternalSerializationApi
fun main() {

    CsModelGen().generate(User.serializer())
    CsModelGen().generate(Users.serializer())
}

