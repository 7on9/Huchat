var db = require("./database");

var  room = {
    getListRoomOfUser : (userName, callback) =>{
        return db.query("CALL PROC_GET_LIST_ROOM_OF_USER(?)", [userName], callback);
    },
    getHistoryOfChatRoom : (roomCode, callback) =>{
        return db.query("CALL PROC_GET_HISTORY_OF_CHAT_ROOM(?)", [roomCode], callback);
    },
    userChat : (roomCode, userName, content, callback) => {
        return db.query("CALL PROC_SEND_MESSAGE(?, ?, ?)", [roomCode, userName, content], callback);
    },
    joinRoom : (roomCode, userName, password, callback) =>{
        return db.query("");
    },
    leaveRoom : (roomCode, userName, callback) =>{
        return db.query("");
    }  
};

module.exports = room;