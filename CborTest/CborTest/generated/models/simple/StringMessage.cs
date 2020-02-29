using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class StringMessage: models.simple.IMessage {
    public int id {set; get;} 
    public string message {set; get;} 

    public override string ToString() {
        return $"id:{id}, message:{message}";
    }
}

class StringMessageConverter: ICBORToFromConverter<StringMessage> {
    public static readonly StringMessageConverter INSTANCE = new StringMessageConverter();
    public StringMessage FromCBORObject(CBORObject obj) => new StringMessage {
        id = obj["id"].AsInt32(),
        message = obj["message"].AsString()
    };
    public CBORObject ToCBORObject(StringMessage model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("message", model.message);
        return obj;
    }
}
}
