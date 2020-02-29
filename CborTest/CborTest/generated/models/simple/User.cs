using System;
using System.Collections.Generic;
using PeterO.Cbor;
using Piisu.CBOR;
namespace models.simple {
class User {
    public int id {set; get;} 
    public string name {set; get;} 
    public List<models.simple.User> likeUsers {set; get;} 

    public override string ToString() {
        return $"id:{id}, name:{name}, likeUsers:{likeUsers}";
    }
}

class UserConverter: ICBORToFromConverter<User> {
    public static readonly UserConverter INSTANCE = new UserConverter();
    public User FromCBORObject(CBORObject obj) => new User {
        id = obj["id"].AsInt32(),
        name = obj["name"].AsString(),
        likeUsers = obj["likeUsers"].ToList(models.simple.UserConverter.INSTANCE)
    };
    public CBORObject ToCBORObject(User model) {
        CBORObject obj = CBORObject.NewMap();
        obj.Add("id", model.id);
        obj.Add("name", model.name);
        obj.Add("likeUsers", model.likeUsers.ToCBORArray(models.simple.UserConverter.INSTANCE));
        return obj;
    }
}
}
