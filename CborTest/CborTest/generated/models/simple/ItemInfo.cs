using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class ItemInfo {
    public int id {set; get;} 
    public string name {set; get;} 
    public DateTime created {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, created:{created}";
    }
}

class ItemInfoConverter: ICBORToFromConverter<ItemInfo> {
    public static readonly ItemInfoConverter INSTANCE = new ItemInfoConverter();
    public ItemInfo FromCBORObject(CBORObject obj) => new ItemInfo {
        id = obj["id"].AsInt32(),
        name = obj["name"].AsString(),
        created = obj["created"].AsDateTime()
    };
    public CBORObject ToCBORObject(ItemInfo model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("created", model.created.ToLong());
        return obj;
    }
}
}
