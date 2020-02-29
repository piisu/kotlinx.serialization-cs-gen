using System;
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
        
        public static DateTime AsDateTime(this CBORObject obj)
        {
            return new DateTime(obj.AsInt64Value());
        }
        
        public static long ToLong(this DateTime dateTime)
        {
            return (long) (dateTime - new DateTime(1970, 1, 1, 0, 0, 0)).TotalMilliseconds;
        }
    }
}
