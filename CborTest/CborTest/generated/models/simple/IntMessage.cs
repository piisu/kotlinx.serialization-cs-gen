using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class IntMessage: models.simple.IMessage {
    public int id {set; get;} 
    public int message {set; get;} 

    public override string ToString() {
        return $"id:{id}, message:{message}";
    }
}

class IntMessageConverter: ICBORToFromConverter<IntMessage> {
    public static readonly IntMessageConverter INSTANCE = new IntMessageConverter();
    public IntMessage FromCBORObject(CBORObject obj) => new IntMessage {
        id = obj["id"].AsInt32(),
        message = obj["message"].AsInt32()
    };
    public CBORObject ToCBORObject(IntMessage model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("message", model.message);
        return obj;
    }
}
}
