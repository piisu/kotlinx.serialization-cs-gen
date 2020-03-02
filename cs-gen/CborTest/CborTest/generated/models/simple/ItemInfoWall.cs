using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfoWall: models.simple.ItemInfoBase, models.simple.ItemInfoEquipment {
    public int layer {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, saleDuration:{saleDuration}, created:{created}, layer:{layer}";
    }
}

class ItemInfoWallConverter: ICBORToFromConverter<ItemInfoWall> {
    public static readonly ItemInfoWallConverter INSTANCE = new ItemInfoWallConverter();
    public ItemInfoWall FromCBORObject(CBORObject obj) => new ItemInfoWall {
        id = obj["id"].ToObject<int>(),
        name = obj["name"].AsString(),
        saleDuration = obj["saleDuration"].ToObject<models.simple.Duration>(models.simple.DurationConverter.INSTANCE),
        created = obj["created"].AsDateTime(),
        layer = obj["layer"].ToObject<int>()
    };
    public CBORObject ToCBORObject(ItemInfoWall model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("saleDuration", models.simple.DurationConverter.INSTANCE.ToCBORObject(model.saleDuration));
        obj.Add("created", model.created.ToLong());
        obj.Add("layer", model.layer);
        return obj;
    }
}
}
