using System;
using System.Collections.Generic;
using System.Linq;
using PeterO.Cbor;

namespace Piisu.CBOR
{
    class ReferenceListConverter<T> : ICBORToFromConverter<List<T>>
    {
        private ICBORToFromConverter<T> itemConverter;

        public ReferenceListConverter(ICBORToFromConverter<T> itemConverter)
        {
            this.itemConverter = itemConverter;
        }
        public CBORObject ToCBORObject(List<T> obj)
        {
            var len = obj.Count;
            var array = CBORObject.NewArray();
            for (int i = 0; i < len; i++)
            {
                array.Add(itemConverter.ToCBORObject(obj[i]));
            }
            return array;
        }

        public List<T> FromCBORObject(CBORObject obj)
        {
            var len = obj.Count;
            var list = new List<T>(len);
            for (int i = 0; i < len; i++)
            {
                list[i] = itemConverter.FromCBORObject(obj[i]);
            }
            return list;
        }
    }

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


        public static T[] ToArray<T>(this CBORObject obj)
        {
            T[] ret = new T[obj.Values.Count];
            for (var i = ret.Length - 1; i >= 0; i--)
            {
                ret[i] = obj[i].ToObject<T>();
            }
            return ret;
        }

        public static T ToObject<T>(this CBORObject obj, ICBORToFromConverter<T> converter)
        {
            return converter.FromCBORObject(obj);
        }
        
        public static long ToLong(this DateTime dateTime)
        {
            return (long) (dateTime - new DateTime(1970, 1, 1, 0, 0, 0)).TotalMilliseconds;
        }
    }
}