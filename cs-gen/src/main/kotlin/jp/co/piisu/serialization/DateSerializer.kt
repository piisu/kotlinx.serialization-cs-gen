package serialization.jp.co.piisu.serialization

import kotlinx.serialization.*
import kotlinx.serialization.internal.StringDescriptor
import java.util.*


@Serializer(forClass = Date::class)
object DateSerializer: KSerializer<Date> {
    override val descriptor: SerialDescriptor
        get() = StringDescriptor.withName("Date Serializer")

    override fun serialize(encoder: Encoder, obj: Date) {
        encoder.encodeLong(obj.time)
    }

    override fun deserialize(decoder: Decoder): Date {
        return Date(decoder.decodeLong())
    }
}
