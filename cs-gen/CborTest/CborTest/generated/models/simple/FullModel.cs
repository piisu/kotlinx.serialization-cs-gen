using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class FullModel {
    public int[] intArray {set; get;} 
    public byte[] byteArray {set; get;} 

    public override string ToString() {
        return $"intArray:{intArray}, byteArray:{byteArray}";
    }
}

class FullModelConverter: ICBORToFromConverter<FullModel> {
    public static readonly FullModelConverter INSTANCE = new FullModelConverter();
    public FullModel FromCBORObject(CBORObject obj) => new FullModel {
        intArray = obj["intArray"].ToArray<int>(),
        byteArray = obj["byteArray"].ToArray<byte>()
    };
    public CBORObject ToCBORObject(FullModel model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("intArray", model.intArray);
        obj.Add("byteArray", model.byteArray);
        return obj;
    }
}
}
