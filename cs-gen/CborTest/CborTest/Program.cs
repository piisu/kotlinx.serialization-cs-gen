using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Text;
using Microsoft.VisualBasic.CompilerServices;
using models.simple;
using PeterO.Cbor;
using Piisu.CBOR;

namespace CborTest
{
    class Program
    {
        static void Main(string[] args)
        {
            var itemInfo = new ItemInfoBath
            {
                id = 123, name = "お風呂", saleDuration = new Duration
                {
                    start = new DateTime(2000, 1, 1),
                    end = new DateTime(2030, 1, 1)
                },
                value = 123
            };


            using (var s = new FileStream("itemInfo.cbor", FileMode.OpenOrCreate))
            {
                ItemInfoBathConverter.INSTANCE.ToCBORObject(itemInfo).WriteTo(s);
            }
            
        }
    }
}