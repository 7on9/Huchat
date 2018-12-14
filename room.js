var db = require("./database");

var  room = {
    getListRoomOfUser : (userName, callback) =>{
        return db.query("CALL PROC_GET_LIST_ROOM_OF_USER(?)", [userName], callback);
    },
    joinRoom : (roomCode, userName, password, callback) =>{
        return db.query("");
    },
    leaveRoom : (roomCode, userName, callback) =>{
        return db.query("");
    }  
};

module.exports = room;