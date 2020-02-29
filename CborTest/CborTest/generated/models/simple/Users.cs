using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class Users {
    public List<models.simple.User> users {set; get;} 

    public override string ToString() {
        return $"users:{users}";
    }
}

class UsersConverter: ICBORToFromConverter<Users> {
    public static readonly UsersConverter INSTANCE = new UsersConverter();
    public Users FromCBORObject(CBORObject obj) {
        Users model = new Users();
        model.users = obj["v"].ToList(models.simple.UserConverter.INSTANCE);
        return model;
    }
    public CBORObject ToCBORObject(Users model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("v", model.users.ToCBORArray(models.simple.UserConverter.INSTANCE));
        return obj;
    }
}
}
