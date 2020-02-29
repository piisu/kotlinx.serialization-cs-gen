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

namespace CborTest
{
    class Program
    {
        static void Main(string[] args)
        {
            User taro = new User();
            taro.id = 1;
            taro.name = "太郎";

            User jiro = new User();
            jiro.id = 2;
            jiro.name = "次郎";

            taro.likeUsers = new List<User>();
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);
            taro.likeUsers.Add(jiro);

            Console.WriteLine(taro);

            CBORObject obj = UserConverter.INSTANCE.ToCBORObject(taro);
            Console.WriteLine(obj.ToJSONString().Length);

            string fileName = "test.cbor";

            FileStream s = new FileStream(fileName, FileMode.OpenOrCreate);
            obj.WriteTo(s);
            Console.WriteLine(s.Length);
            s.Close();
        }
    }
}