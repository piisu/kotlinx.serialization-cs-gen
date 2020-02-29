using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
interface IMessage {
    int id {get; set;}

}

class IMessageConverter: ICBORToFromConverter<IMessage> {
    public static readonly IMessageConverter INSTANCE = new IMessageConverter();
    public IMessage FromCBORObject(CBORObject obj) {
        switch(obj["class"].AsString()) {
        case "IntM":
            return models.simple.IntMessageConverter.INSTANCE.FromCBORObject(obj["value"]);
        case "models.simple.StringMessage":
            return models.simple.StringMessageConverter.INSTANCE.FromCBORObject(obj["value"]);
        }
        return null;
    }
    public CBORObject ToCBORObject(IMessage model) {
        switch(model) {
        case models.simple.IntMessage v:
            return CBORObject.NewMap().Add("class", "IntM")
                .Add("value", models.simple.IntMessageConverter.INSTANCE.ToCBORObject(v));
        case models.simple.StringMessage v:
            return CBORObject.NewMap().Add("class", "models.simple.StringMessage")
                .Add("value", models.simple.StringMessageConverter.INSTANCE.ToCBORObject(v));
        }
        return null;
    }
}
}
