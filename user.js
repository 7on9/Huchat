var db = require("./database");

var user = {
    
    // isExistUserName: async function (user_name) {
    //         var x = await fnIsExistUserName(user_name);
    //     return  (x.length > 0) ? true : false;       
    //     //console.log(resq);
    //     //console.log((resq == user_name) ? true : false); 
    //     //return true;//return (resq == user_name) ? true : false; 
    // },
    getUserById: function (id, callback) {
        return db.query("SELECT * FROM account WHERE ID = ?", [id], callback);
    },
    login: function (user_name, pass, callback) {
        return db.query("select * from account where user_name = ? and password = ?", [user_name], [pass], callback);
    },
    addInfoUser: function (user, callback) {
        db.query("select * from account where user_name = ?", [user.mail], function (err, res) {
            if (!err) {
                if (res.length > 0) {
                    return db.query("select ?", [user.mail], callback);
                } else {
                    return db.query("insert into user(name, birth, gender, phone, mail, pass, type, lastLongitude, lastLatitude) values(?, ?, ?, ?, ?, ?, ?, ?, ?)", [user.name, user.birth, user.gender, user.phone, user.mail, user.pass, user.type, user.lastLongitude, user.lastLatitude], callback);
                }
            }
        });
    },
    updateInfoUser: function (id, user, callback) {
        db.query("select * from user where mail = ?", [user.mail], function (err, res) {
            if (!err) {
                if (res.id != id) {
                    return db.query("select ?", [user.mail], callback);
                } else {
                    return db.query("update user set name = ?, birth = ?, gender = ?, phone = ?, mail = ?, pass = ?, type = ?, lastLongitude = ?, lastLatitude = ?, rating = ? where Id = ?", [user.name, user.birth, user.gender, user.phone, user.mail, user.pass, user.type, user.lastLongitude, user.lastLatitude, user.rating, id], callback);
                }
            }
        });
    }
};
module.exports = user;