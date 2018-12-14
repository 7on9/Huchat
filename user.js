var db = require("./database");

var user = {
    // isExistUserName: async function (user_name) {
    //         var x = await fnIsExistUserName(user_name);
    //     return  (x.length > 0) ? true : false;       
    //     //console.log(resq);
    //     //console.log((resq == user_name) ? true : false); 
    //     //return true;//return (resq == user_name) ? true : false; 
    // },
    updateMail : (user_name, mail, callback) => {
        return db.query("UPDATE USERS SET MAIL = ? WHERE USER_NAME = ?", [mail], [user_name], callback);
    },
    updateFullName : (user_name, fullName, callback) => {
        return db.query("UPDATE USERS SET FULL_NAME = ? WHERE USER_NAME = ?", [fullName], [user_name], callback);
    },
    updateInfoUser : (aUser, callback) => {
        return db.query("CALL PROC_UPDATE_INFO_USER(?, ?, ?, ?, ?, ?, ?", [aUser.user_name], [aUser.fullName], [aUser.dob], [aUser.gender], [aUser.mail], [aUser.phone], [aUser.avatarPath], callback);
    }
};
module.exports = user;