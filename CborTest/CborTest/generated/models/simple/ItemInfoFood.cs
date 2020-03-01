using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfoFood: models.simple.ItemInfoBase {
    public int id {set; get;} 
    public string name {set; get;} 
    public models.simple.Duration saleDuration {set; get;} 
    public int value {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, saleDuration:{saleDuration}, value:{value}";
    }
}

class ItemInfoFoodConverter: ICBORToFromConverter<ItemInfoFood> {
    public static readonly ItemInfoFoodConverter INSTANCE = new ItemInfoFoodConverter();
    public ItemInfoFood FromCBORObject(CBORObject obj) => new ItemInfoFood {
        id = obj["id"].ToObject<int>(),
        name = obj["name"].AsString(),
        saleDuration = obj["saleDuration"].ToObject<models.simple.Duration>(models.simple.DurationConverter.INSTANCE),
        value = obj["value"].ToObject<int>()
    };
    public CBORObject ToCBORObject(ItemInfoFood model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("saleDuration", models.simple.DurationConverter.INSTANCE.ToCBORObject(model.saleDuration));
        obj.Add("value", model.value);
        return obj;
    }
}
}
