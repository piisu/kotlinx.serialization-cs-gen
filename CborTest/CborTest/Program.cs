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


namespace models.simple
{
    using Piisu.CBOR;

    class User
    {
        public int id { set; get; }
        public string name { set; get; }
        public List<models.simple.User> likeUsers { set; get; }

        public override string ToString()
        {
            return $"id:{id}, name:{name}, likeUsers:{likeUsers}";
        }
    }

    class UserConverter : ICBORToFromConverter<User>
    {
        public static readonly UserConverter INSTANCE = new UserConverter();

        public User FromCBORObject(CBORObject obj)
        {
            User model = new User();
            model.id = obj["id"].AsInt32();
            model.name = obj["name"].AsString();
            model.likeUsers = obj["likeUsers"].ToList(models.simple.UserConverter.INSTANCE);
            return model;
        }

        public CBORObject ToCBORObject(User model)
        {
            CBORObject obj = CBORObject.NewMap();
            obj.Add("id", model.id);
            obj.Add("name", model.name);
            obj.Add("likeUsers", model.likeUsers.ToCBORArray(models.simple.UserConverter.INSTANCE));
            return obj;
        }
    }
    
    class Users {
        public List<models.simple.User> users {set; get;} 

        public override string ToString() {
            return $"users:{users}";
        }
    }

    [GeneratedCode("hoge", "1.0")]
    class UsersConverter: ICBORToFromConverter<Users> {
        public static readonly UsersConverter INSTANCE = new UsersConverter();
        public Users FromCBORObject(CBORObject obj) {
            Users model = new Users();
            model.users = obj["users"].ToList(models.simple.UserConverter.INSTANCE);
            return model;
        }
        public CBORObject ToCBORObject(Users model) {
            CBORObject obj = CBORObject.NewMap();
            obj.Add("users", model.users.ToCBORArray(models.simple.UserConverter.INSTANCE));
            return obj;
        }
    }
}

namespace CborTest
{
    class Program
    {
        static void Main(string[] args)
        {
            User taro = new User();
            taro.id = 1;
            taro.name = "Taro";

            User jiro = new User();
            jiro.id = 2;
            jiro.name = "Jiro";

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

            string fileName = "/tmp/test.cbor";

            FileStream s = new FileStream(fileName, FileMode.OpenOrCreate);
            obj.WriteTo(s);
            Console.WriteLine(s.Length);
            s.Close();
        }
    }
}