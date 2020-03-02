using System;
using System.CodeDom.Compiler;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.IO.Compression;
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
            var itemInfoMaster = new ItemInfoMaster
            {
                items = new List<ItemInfo>
                {
                    new ItemInfoBath
                    {
                        id = 123, name = "お風呂", saleDuration = new Duration
                        {
                            start = new DateTime(2000, 1, 1),
                            end = new DateTime(2030, 1, 1)
                        },
                        value = 123, created = DateTime.Now
                    },
                    new ItemInfoFood
                    {
                        id = 234, name="カレーライス", saleDuration = new Duration
                        {
                            start = new DateTime(2000, 1, 1),
                            end = new DateTime(2030, 1, 1)
                        },
                        value = 123, created = DateTime.Now
                    }
                }
            };

            for (int i = 0; i < 10; i++)
            {
                itemInfoMaster.items.AddRange(itemInfoMaster.items);
            }
            
            
            using (var s = new FileStream("itemMaster.json", FileMode.OpenOrCreate))
            {
                s.Write(ItemInfoMasterConverter.INSTANCE.ToCBORObject(itemInfoMaster).ToJSONBytes());
            }
            
            using (var s = new FileStream("itemMaster.cbor", FileMode.OpenOrCreate))
            {
                using (var z = new ZipArchive(s, ZipArchiveMode.Create))
                {
                    z.CreateEntry("entry", CompressionLevel.Fastest).Open()
                        .Write(ItemInfoMasterConverter.INSTANCE.ToCBORObject(itemInfoMaster).EncodeToBytes());
                }

                
            }
            
        }
    }
}