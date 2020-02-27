package jp.co.piisu.serialization

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.*
import kotlinx.serialization.modules.SerialModule

@InternalSerializationApi
class CsModelGen(val context: SerialModule) {

    inner class Property(
            val name: String, val descriptor: SerialDescriptor
            , val annotations: List<Annotation>, val serializer: KSerializer<*>
    ) {
        override fun toString(): String {
            return serializer.csType + " " + name
        }

        val csField: String
            get() = "${serializer.csType} ${name} {set; get;} "
    }

    fun generate(serializer: KSerializer<*>) {
        serializer as GeneratedSerializer<*>
        val childSerializers = serializer.childSerializers()
        val properties = (0 until serializer.descriptor.elementsCount).map { index ->
            serializer.descriptor.let {
                Property(
                        it.getElementName(index),
                        it.getElementDescriptor(index),
                        it.getElementAnnotations(index),
                        childSerializers[index]
                )
            }
        }
        println(properties.map { it.csField }.joinToString("\n"))
    }

    val ListLikeSerializer<*, *, *>.elementSerializer
        get() = ListLikeSerializer::class.java.getDeclaredField("elementSerializer")!!.let {
            it.isAccessible = true
            it.get(this) as KSerializer<*>
        }

    val KSerializer<*>.csType: String
        get() = when {
            this == IntArraySerializer -> "int[]"
            this == ByteArraySerializer -> "byte[]"
            this == CharArraySerializer -> "char[]"
            this == ShortArraySerializer -> "short[]"
            this == LongArraySerializer -> "long[]"
            this == DoubleArraySerializer -> "double[]"
            this == FloatArraySerializer -> "float[]"
            this == BooleanArraySerializer -> "bool[]"
            this == StringSerializer -> "string"
            this == IntSerializer -> "int"
            this == ByteSerializer -> "byte"
            this == CharSerializer -> "char"
            this == ShortSerializer -> "short"
            this == LongSerializer -> "long"
            this == DoubleSerializer -> "double"
            this == FloatSerializer -> "float"
            this == BooleanSerializer -> "bool"
            this is ReferenceArraySerializer<*, *> -> "List<${elementSerializer.csType}>"
            this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
            this is GeneratedSerializer<*> -> descriptor.name
            else -> "UnknowType" + this.toString()
        }
}