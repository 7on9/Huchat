var db = require("./database");

var account = {
    checkExistUserName: function (user_name, callback) {
        var query = "SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME = " + db.escape(user_name);
        return db.query(query, callback);
    },
    login: function(user_name, password, callback){
        return db.query("SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME = ? AND PASSWORD = ?", [user_name], [password], callback);
    },
    addAccount: function (user_name, password, callback) {
        db.query("SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME = ?", [user_name], function (err, res) {
            if (!err) {
                if (res.length > 0) {
                    return db.query("SELECT ?", [user_name], callback);
                } else {
                    return db.query("CALL PROC_INSERT_ACCOUNT (?, ?)", [user_name],[password], callback);
                }
            }
        });
    },
    updateAccountPass: function (user_name, password, callback) {
        db.query("SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME = ?", [user_name], function (err, res) {
            if (!err) {
                if (res.length == 0) {
                    return db.query("SELECT -1", callback);
                } else {
                    return db.query("CALL PROC_CHANGE_PASSWORD_ACCOUNT (?, ?)", [password], [user_name], callback);
                }
            }
        });
    },
    deleteAccount: function (user_name, callback){
        return db.query("CALL PROC_DELETE_ACCOUNT (?)", [user_name], callback);
    }

}
module.exports = account;