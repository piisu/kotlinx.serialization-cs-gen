using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfoFood: models.simple.ItemInfoBase {
    public int value {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, saleDuration:{saleDuration}, created:{created}, value:{value}";
    }
}

class ItemInfoFoodConverter: ICBORToFromConverter<ItemInfoFood> {
    public static readonly ItemInfoFoodConverter INSTANCE = new ItemInfoFoodConverter();
    public ItemInfoFood FromCBORObject(CBORObject obj) => new ItemInfoFood {
        id = obj["id"].ToObject<int>(),
        name = obj["name"].AsString(),
        saleDuration = obj["saleDuration"].ToObject<models.simple.Duration>(models.simple.DurationConverter.INSTANCE),
        created = obj["created"].AsDateTime(),
        value = obj["value"].ToObject<int>()
    };
    public CBORObject ToCBORObject(ItemInfoFood model) {
        return CBORObject.NewMap()
            .Add()
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("saleDuration", models.simple.DurationConverter.INSTANCE.ToCBORObject(model.saleDuration));
        obj.Add("created", model.created.ToLong());
        obj.Add("value", model.value);
        return obj;
    }
}
}
