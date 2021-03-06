using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfoBath: models.simple.ItemInfoBase {
    public int value {set; get;} 
    public int interval {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, saleDuration:{saleDuration}, created:{created}, value:{value}, interval:{interval}";
    }
}

class ItemInfoBathConverter: ICBORToFromConverter<ItemInfoBath> {
    public static readonly ItemInfoBathConverter INSTANCE = new ItemInfoBathConverter();
    public ItemInfoBath FromCBORObject(CBORObject obj) => new ItemInfoBath {
        id = obj["id"].ToObject<int>(),
        name = obj["name"].AsString(),
        saleDuration = obj["saleDuration"].ToObject<models.simple.Duration>(models.simple.DurationConverter.INSTANCE),
        created = obj["created"].AsDateTime(),
        value = obj["value"].ToObject<int>(),
        interval = obj["interval"].ToObject<int>()
    };
    public CBORObject ToCBORObject(ItemInfoBath model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("saleDuration", models.simple.DurationConverter.INSTANCE.ToCBORObject(model.saleDuration));
        obj.Add("created", model.created.ToLong());
        obj.Add("value", model.value);
        obj.Add("interval", model.interval);
        return obj;
    }
}
}
