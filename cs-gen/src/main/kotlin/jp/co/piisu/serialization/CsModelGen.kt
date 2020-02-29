package jp.co.piisu.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.*
import kotlinx.serialization.modules.EmptyModule
import kotlinx.serialization.modules.SerialModule
import serialization.jp.co.piisu.serialization.DateSerializer
import java.io.File
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KType
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

    val KType.csType:String
        get() = when(classifier) {
            Int::class-> "int"
            Date::class -> "DateTime"
            else -> "Unknown: " + this
        }


    internal val kotlin.reflect.KProperty<*>.csField:String
        get()= "${returnType.csType} ${name} {get; set;}"



    @ExperimentalStdlibApi
    fun generatePolymorphic() {
        val polyMap = context.javaClass.declaredFields.find { it.name=="polyMap" }!!.let {
            it.isAccessible = true
            it.get(context)
        } as Map<KClass<*>, Map<KClass<*>, KSerializer<*>>>

        polyMap.forEach { clazz, map ->

            map.forEach { clazz, s->
                this.generate(clazz, s)
            }

            val outFile = File(dstDir, clazz.qualifiedName!!.replace(".", File.separator) + ".cs")
            outFile.parentFile.mkdirs()
            outFile.printWriter(Charsets.UTF_8).use {
                val modelName = clazz.simpleName

                it.println("""
                using System;
                using System.Collections.Generic;
                using PeterO.Cbor;
                using Piisu.CBOR;
            """.trimIndent())
                it.println("namespace models.simple {")
                val inheritClasses = clazz.supertypes.filter { it.classifier != Any::class }.map { (it.classifier as KClass<*>).qualifiedName }
                        .let {
                            if (it.isEmpty()) "" else it.joinToString(", ", prefix = ": ")
                        }
                it.println("interface ${modelName}${inheritClasses} {")
                it.println(clazz.memberProperties.map { "${it.csField}" }.joinToString("\n    ", prefix = "    "))
                it.println()
                it.println("}")


                val converterName = "${modelName}Converter"
                it.println()
                it.println("class ${converterName}: ICBORToFromConverter<${modelName}> {")
                it.println("    public static readonly ${converterName} INSTANCE = new ${converterName}();")
                it.println("    public ${modelName} FromCBORObject(CBORObject obj) {")
                it.println("        switch(obj[\"class\"].AsString()) {")
                map.forEach { subClass, serializer ->
                    it.println("        case \"${serializer.descriptor.name}\":")
                    it.println("            return ${subClass.qualifiedName}Converter.INSTANCE.FromCBORObject(obj[\"value\"]);")
                }
                it.println("        }")
                it.println("        return null;")
                it.println("    }")

                it.println("    public CBORObject ToCBORObject(${modelName} model) {")
                it.println("        switch(model) {")
                map.forEach { subClass, serializer ->
                    it.println("        case ${subClass.qualifiedName} v:")
                    it.println("            return CBORObject.NewMap().Add(\"class\", \"${serializer.descriptor.name}\")")
                    it.println("                .Add(\"value\", ${subClass.qualifiedName}Converter.INSTANCE.ToCBORObject(v));")
                }
                it.println("        }")
                it.println("        return null;")
                it.println("    }")
                it.println("}")
                it.println("}")
            }
        }
    }


    @ExperimentalStdlibApi
    inline fun <reified T : Any> generate(serializer: KSerializer<T>) {
        generate(T::class, serializer)
    }

    @ExperimentalStdlibApi
    fun generate(clazz:KClass<*>, serializer:KSerializer<*>) {
        serializer as GeneratedSerializer

        val inversePropertyName = clazz.memberProperties.filter {
            it.hasAnnotation<SerialName>()
        }.map { it.findAnnotation<SerialName>()!!.value to it.name }.toMap()

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

        val outFile = File(dstDir, clazz.qualifiedName!!.replace(".", File.separator) + ".cs")
        outFile.parentFile.mkdirs()

        val modelName = clazz.simpleName
        outFile.printWriter(Charsets.UTF_8).use {
            it.println("""
                using System;
                using System.Collections.Generic;
                using PeterO.Cbor;
                using Piisu.CBOR;
            """.trimIndent())
            it.println("namespace models.simple {")
            val inheritClasses = clazz.supertypes.filter { it.classifier != Any::class }.map { (it.classifier as KClass<*>).qualifiedName }
                    .let {
                        if (it.isEmpty()) "" else it.joinToString(", ", prefix = ": ")
                    }
            it.println("class ${modelName}${inheritClasses} {")
            it.println(properties.map { "${it.csField}" }.joinToString("\n    ", prefix = "    "))
            it.println()
            it.println("    public override string ToString() {")
            it.println("        return $\"" + properties.map { "${it.name}:{${it.name}}" }.joinToString() + "\";")
            it.println("    }")
            it.println("}")


            val converterName = "${modelName}Converter"
            it.println()
            it.println("class ${converterName}: ICBORToFromConverter<${modelName}> {")
            it.println("    public static readonly ${converterName} INSTANCE = new ${converterName}();")

            it.println("    public ${modelName} FromCBORObject(CBORObject obj) => new ${modelName} {")
            it.println(properties.map { it.readOperation }
                    .joinToString(",\n        ", prefix = "        "))
            it.println("    };")

            it.println("    public CBORObject ToCBORObject(${modelName} model) {")
            it.println("        CBORObject obj = CBORObject.NewMap();")

            it.println(properties.map { it.writeOperation }
                    .joinToString("\n        ", prefix = "        "))
            it.println("        return obj;")
            it.println("    }")
            it.println("}")
            it.println("}")
        }

    }

    val ListLikeSerializer<*, *, *>.elementSerializer
        get() = ListLikeSerializer::class.java.getDeclaredField("elementSerializer")!!.let {
            it.isAccessible = true
            it.get(this) as KSerializer<*>
        }

    fun KSerializer<*>.genReadOperation(name: String, serialName: String): String = "${name} = obj[\"${serialName}\"]." + when {
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
        this == DateSerializer -> "AsDateTime()"
        this is ReferenceArraySerializer<*, *> -> "ToList(${elementSerializer.csType}Converter.INSTANCE)"
        this is ArrayListSerializer<*> -> "ToList(${elementSerializer.csType}Converter.INSTANCE)"
        this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
        this is GeneratedSerializer<*> -> descriptor.name
        else -> "UnknowType:" + this.toString()
    } + ""

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
        this == DateSerializer -> "model.${name}.ToLong()"
        this is ReferenceArraySerializer<*, *> -> "model.${name}.ToCBORArray(${elementSerializer.csType}Converter.INSTANCE)"
        this is ArrayListSerializer<*> -> "model.${name}.ToCBORArray(${elementSerializer.csType}Converter.INSTANCE)"
        this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
        this is GeneratedSerializer<*> -> descriptor.name
        else -> "UnknowType:" + this.toString()
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
            this == DateSerializer -> "DateTime"
            this is ReferenceArraySerializer<*, *> -> "List<${elementSerializer.csType}>"
            this is ArrayListSerializer<*> -> "List<${elementSerializer.csType}>"
            this is PolymorphicSerializer<*> -> baseClass.qualifiedName!!
            this is GeneratedSerializer<*> -> descriptor.name
            else -> "UnknowType:" + this.toString()
        }
}