using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
interface ItemInfo {
    int id {get; set;}
    string name {get; set;}
    models.simple.Duration saleDuration {get; set;}

}

class ItemInfoConverter: ICBORToFromConverter<ItemInfo> {
    public static readonly ItemInfoConverter INSTANCE = new ItemInfoConverter();
    public ItemInfo FromCBORObject(CBORObject obj) {
        switch(obj["class"].AsString()) {
        case "models.simple.ItemInfoBath":
            return models.simple.ItemInfoBathConverter.INSTANCE.FromCBORObject(obj["value"]);
        case "models.simple.ItemInfoFood":
            return models.simple.ItemInfoFoodConverter.INSTANCE.FromCBORObject(obj["value"]);
        }
        return null;
    }
    public CBORObject ToCBORObject(ItemInfo model) {
        switch(model) {
        case models.simple.ItemInfoBath v:
            return CBORObject.NewMap().Add("class", "models.simple.ItemInfoBath")
                .Add("value", models.simple.ItemInfoBathConverter.INSTANCE.ToCBORObject(v));
        case models.simple.ItemInfoFood v:
            return CBORObject.NewMap().Add("class", "models.simple.ItemInfoFood")
                .Add("value", models.simple.ItemInfoFoodConverter.INSTANCE.ToCBORObject(v));
        }
        return null;
    }
}
}
