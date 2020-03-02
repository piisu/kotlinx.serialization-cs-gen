using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfoBath: models.simple.ItemInfoBase {
    public int id {set; get;} 
    public string name {set; get;} 
    public models.simple.Duration saleDuration {set; get;} 
    public int value {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, saleDuration:{saleDuration}, value:{value}";
    }
}

class ItemInfoBathConverter: ICBORToFromConverter<ItemInfoBath> {
    public static readonly ItemInfoBathConverter INSTANCE = new ItemInfoBathConverter();
    public ItemInfoBath FromCBORObject(CBORObject obj) => new ItemInfoBath {
        id = obj["id"].ToObject<int>(),
        name = obj["name"].AsString(),
        saleDuration = obj["saleDuration"].ToObject<models.simple.Duration>(models.simple.DurationConverter.INSTANCE),
        value = obj["value"].ToObject<int>()
    };
    public CBORObject ToCBORObject(ItemInfoBath model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("saleDuration", models.simple.DurationConverter.INSTANCE.ToCBORObject(model.saleDuration));
        obj.Add("value", model.value);
        return obj;
    }
}
}
