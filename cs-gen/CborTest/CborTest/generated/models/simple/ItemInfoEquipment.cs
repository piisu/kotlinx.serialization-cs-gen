using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
interface ItemInfoEquipment: models.simple.ItemInfo {
    int layer {get; set;}

}

class ItemInfoEquipmentConverter: ICBORToFromConverter<ItemInfoEquipment> {
    public static readonly ItemInfoEquipmentConverter INSTANCE = new ItemInfoEquipmentConverter();
    public ItemInfoEquipment FromCBORObject(CBORObject obj) {
        switch(obj["class"].AsString()) {
        case "models.simple.ItemInfoWall":
            return models.simple.ItemInfoWallConverter.INSTANCE.FromCBORObject(obj["value"]);
        }
        return null;
    }
    public CBORObject ToCBORObject(ItemInfoEquipment model) {
        switch(model) {
        case models.simple.ItemInfoWall v:
            return CBORObject.NewMap().Add("class", "models.simple.ItemInfoWall")
                .Add("value", models.simple.ItemInfoWallConverter.INSTANCE.ToCBORObject(v));
        }
        return null;
    }
}
}
