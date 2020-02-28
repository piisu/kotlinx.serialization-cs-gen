using System.Collections.Generic;
using PeterO.Cbor;

namespace Piisu.CBOR
{
    static class CBORExtension
    {
        public static List<T> ToList<T>(this CBORObject obj, ICBORToFromConverter<T> converter)
        {
            List<T> list = new List<T>(obj.Count);
            for (var i = 0; i < obj.Count; i++)
            {
                list.Add(converter.FromCBORObject(obj[i]));
            }

            return list;
        }

        public static CBORObject ToCBORArray<T>(this List<T> list, ICBORToFromConverter<T> converter)
        {
            var array = CBORObject.NewArray();
            list?.ForEach(it => array.Add(converter.ToCBORObject(it)));
            return array;
        }
    }
}
