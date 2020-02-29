using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {

    class Messages {
    public List<models.simple.IMessage> messages {set; get;} 

    public override string ToString() {
        return $"messages:{messages}";
    }
}

class MessagesConverter: ICBORToFromConverter<Messages> {
    public static readonly MessagesConverter INSTANCE = new MessagesConverter();
    public Messages FromCBORObject(CBORObject obj) => new Messages {
        messages = obj["messages"].ToList(models.simple.IMessageConverter.INSTANCE)
    };
    public CBORObject ToCBORObject(Messages model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("messages", model.messages.ToCBORArray(models.simple.IMessageConverter.INSTANCE));
        return obj;
    }
}
}
