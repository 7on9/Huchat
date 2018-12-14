var db = require("./database");

var getUsername = (user_name) => {
    return new Promise(function (resolve, reject) {
        db.query("SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME LIKE ?", [user_name], function (err, rows) {
            if (err) {
                reject(() => console.log(err + ""));
            }
            resolve(rows);
        });
    })
};

var getEmail = (email) => {
    return new Promise(function (resolve, reject) {
        db.query("SELECT MAIL FROM USERS WHERE MAIL LIKE ?", [email], function (err, rows) {
            if (err) {
                reject(() => console.log(err + ""));
            }
            resolve(rows);
        });
    })
};

var account = {
    checkExistUserName: async (user_name) => {
        try {
            let result = await getUsername(user_name);
            return (result.length > 0) ? true : false;
        } catch (ex) {
            console.log(ex);
            return false;
        }
    },
    checkExistEmail: async (email) => {
        try {
            let result = await getEmail(email);
            return (result.length > 0) ? true : false;
        } catch (ex) {
            console.log(ex);
            return false;
        }
    },
    login: function (user_name, password, callback) {
        return db.query("SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME LIKE ? AND PASSWORD = ?", [user_name, password], callback);
    },
    logout: function (user_name, callback) {
        return db.query("CALL PROC_LOGOUT_EVENT(?)", [user_name], callback);
    },
    registerAccount: async function (user_name, password, email, callback) {
        return db.query("CALL PROC_INSERT_ACCOUNT(?, ?, ?);", [user_name, password, email], callback);
    },
    updateAccountPass: async (user_name, password, callback) => {
        try {
            let result = await getUsername(user_name);
            return db.query("CALL PROC_CHANGE_PASSWORD_ACCOUNT (?, ?)", [password, user_name], callback);
        } catch (ex) {
            return db.query("SELECT -1", callback);
        }
    },
    deleteAccount: function (user_name, callback) {
        return db.query("CALL PROC_DELETE_ACCOUNT (?)", [user_name], callback);
    }
}
module.exports = account;