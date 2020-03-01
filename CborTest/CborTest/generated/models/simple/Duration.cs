using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class Duration {
    public DateTime start {set; get;} 
    public DateTime end {set; get;} 

    public override string ToString() {
        return $"start:{start}, end:{end}";
    }
}

class DurationConverter: ICBORToFromConverter<Duration> {
    public static readonly DurationConverter INSTANCE = new DurationConverter();
    public Duration FromCBORObject(CBORObject obj) => new Duration {
        start = obj["start"].AsDateTime(),
        end = obj["end"].AsDateTime()
    };
    public CBORObject ToCBORObject(Duration model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("start", model.start.ToLong());
        obj.Add("end", model.end.ToLong());
        return obj;
    }
}
}
