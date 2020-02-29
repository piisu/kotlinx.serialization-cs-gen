package jp.co.piisu.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.*
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule
import java.io.File
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.superclasses

@InternalSerializationApi
class CsModelGen(val context: SerialModule = EmptyModule, var dstDir: File = File(".")) {

    inner class Property(
            val name: String, val serialName: String, val descriptor: SerialDescriptor
            , val annotations: List<Annotation>, val serializer: KSerializer<*>
    ) {
        val csField: String
            get() = "public ${serializer.csType} ${name} {set; get;} "

        val writeOperation:String
            get() = "${serializer.genWriteOperation(name, serialName)}"

        val readOperation:String
            get() ="${serializer.genReadOperation(name, serialName)}"

    }

    @ExperimentalStdlibApi
    inline fun <reified T : Any> generate(serializer: KSerializer<T>) {
        val clazz = T::class
        serializer as GeneratedSerializer<*>

        val inversePropertyName = clazz.memberProperties.filter {
            it.hasAnnotation<SerialName>()
        }.map { it.findAnnotation<SerialName>()!!.value to it.name }.toMap()

        println(clazz.supertypes.map { it.classifier })
        println(clazz.superclasses.map { it })

        val childSerializers = serializer.childSerializers()
        val properties = (0 until serializer.descriptor.elementsCount).map { index ->
            serializer.descriptor.let {
                val serialName = it.getElementName(index)
                Property(
                        inversePropertyName.getOrDefault(serialName, serialName),
                        serialName,
                        it.getElementDescriptor(index),
                        it.getElementAnnotations(index),
                        childSerializers[index]
                )
            }
        }

        val modelName = clazz.simpleName
        println("class ${modelName} {")
        println(properties.map { "${it.csField}" }.joinToString("\n    ", prefix = "    "))
        println()
        println("    public override string ToString() {")
        println("        return $\"" + properties.map { "${it.name}:{${it.name}}" }.joinToString() + "\";")
        println("    }")
        println("}")


        val converterName = "${modelName}Converter"
        println()
        println("class ${converterName}: ICBORToFromConverter<${modelName}> {")
        println("    public static readonly ${converterName} INSTANCE = new ${converterName}();")

        println("    public ${modelName} FromCBORObject(CBORObject obj) {")
        println("        ${modelName} model = new ${modelName}();")
        println(properties.map { it.readOperation }
                .joinToString("\n        ", prefix = "        "))
        println("        return model;")
        println("    }")

        println("    public CBORObject ToCBORObject(${modelName} model) {")
        println("        CBORObject obj = CBORObject.NewMap();")

        println(properties.map { it.writeOperation }
                .joinToString("\n        ", prefix = "        "))
        println("        return obj;")
        println("    }")

        println("}")
    }

    val ListLikeSerializer<*, *, *>.elementSerializer
        get() = ListLikeSerializer::class.java.getDeclaredField("elementSerializer")!!.let {
            it.isAccessible = true
            it.get(this) as KSerializer<*>
        }

    fun KSerializer<*>.genReadOperation(name: String, serialName: String): String = "model.${name} = obj[\"${serialName}\"]." + when {
        this == IntArraySerializer -> TODO()
        this == ByteArraySerializer -> TODO()
        this == CharArraySerializer -> TODO()
        this == ShortArraySerializer -> TODO()
        this == LongArraySerializer -> TODO()
        this == DoubleArraySerializer -> TODO()
        this == FloatArraySerializer -> TODO()
        this == BooleanArraySerializer -> TODO()
        this == StringSerializer -> "AsString()"
        this == IntSerializer -> "AsInt32()"
        this == ByteSerializer -> TODO()
        this == CharSerializer -> TODO()
        this == ShortSerializer -> TODO()
        this == LongSerializer -> TODO()
        this == DoubleSerializer -> TODO()
        this == FloatSerializer -> TODO()
        this == BooleanSerializer -> TODO()
        this is ReferenceArraySerializer<*, *> -> "ToList(${elementSerializer.csType}Converter.INSTANCE)"
        this is ArrayListSerializer<*> -> "ToList(${elementSerializer.csType}Converter.INSTANCE)"
        this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
        this is GeneratedSerializer<*> -> descriptor.name
        else -> "UnknowType" + this.toString()
    } + ";"

    fun KSerializer<*>.genWriteOperation(name: String, serialName: String): String = "obj.Add(\"${serialName}\", " + when {
        this == IntArraySerializer -> TODO()
        this == ByteArraySerializer -> TODO()
        this == CharArraySerializer -> TODO()
        this == ShortArraySerializer -> TODO()
        this == LongArraySerializer -> TODO()
        this == DoubleArraySerializer -> TODO()
        this == FloatArraySerializer -> TODO()
        this == BooleanArraySerializer -> TODO()
        this == StringSerializer -> "model.${name}"
        this == IntSerializer -> "model.${name}"
        this == ByteSerializer -> TODO()
        this == CharSerializer -> TODO()
        this == ShortSerializer -> TODO()
        this == LongSerializer -> TODO()
        this == DoubleSerializer -> TODO()
        this == FloatSerializer -> TODO()
        this == BooleanSerializer -> TODO()
        this is ReferenceArraySerializer<*, *> -> "model.${name}.ToCBORArray(${elementSerializer.csType}Converter.INSTANCE)"
        this is ArrayListSerializer<*> -> "model.${name}.ToCBORArray(${elementSerializer.csType}Converter.INSTANCE)"
        this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
        this is GeneratedSerializer<*> -> descriptor.name
        else -> "UnknowType" + this.toString()
    } + ");"


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
            this is ArrayListSerializer<*> -> "List<${elementSerializer.csType}>"
            this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
            this is GeneratedSerializer<*> -> descriptor.name
            else -> "UnknowType" + this.toString()
        }
}