using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfoBase: models.simple.ItemInfo {
    public int id {set; get;} 
    public string name {set; get;} 
    public models.simple.Duration saleDuration {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, saleDuration:{saleDuration}";
    }
}

class ItemInfoBaseConverter: ICBORToFromConverter<ItemInfoBase> {
    public static readonly ItemInfoBaseConverter INSTANCE = new ItemInfoBaseConverter();
    public ItemInfoBase FromCBORObject(CBORObject obj) => new ItemInfoBase {
        id = obj["id"].ToObject<int>(),
        name = obj["name"].AsString(),
        saleDuration = obj["saleDuration"].ToObject<models.simple.Duration>(models.simple.DurationConverter.INSTANCE)
    };
    public CBORObject ToCBORObject(ItemInfoBase model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("saleDuration", models.simple.DurationConverter.INSTANCE.ToCBORObject(model.saleDuration));
        return obj;
    }
}
}
