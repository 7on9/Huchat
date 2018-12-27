var db = require("./database");

let callSelectRoom = (roomCode) =>{ 
    return new Promise((resolve, reject) => {
        db.query("SELECT ROOM_CODE FROM ROOMS WHERE ROOM_CODE = ?", [roomCode], (err, rows)=>{
            if(err){
                reject(()=> console.log(err));
            }else{
                resolve(rows);
            }
        });
    })
}

var  room = {
    getAllListRoom : (callback) => {
        return db.query("CALL PROC_GET_ALL_PUBLIC_ROOM()", callback);
    },
    getListRoomOfUser : (userName, callback) =>{
        return db.query("CALL PROC_GET_LIST_ROOM_OF_USER(?)", [userName], callback);
    },
    getListMemberOfRoom : (roomCode, callback) =>{
        return db.query("CALL PROC_GET_LIST_MEMBER_OF_ROOM(?)", [roomCode], callback);
    },
    getPublicInfoOfRoom : (roomCode, callback) =>{
        return db.query("CALL PROC_GET_PUBLIC_INFO_OF_ROOM(?)", [roomCode], callback);
    },
    getHistoryOfChatRoom : (roomCode, callback) =>{
        return db.query("CALL PROC_GET_HISTORY_OF_CHAT_ROOM(?)", [roomCode], callback);
    },
    userChat : (roomCode, userName, content, callback) => {
        return db.query("CALL PROC_SEND_MESSAGE(?, ?, ?)", [roomCode, userName, content], callback);
    },
    checkExistRoom : async (roomCode) =>{
        try {
            let result = await callSelectRoom(roomCode);
            return (result.length > 0) ? true : false;
        } catch (ex) {
            console.log(ex);
            return false;
        }
    },
    createDualRoom : async(userName, userName2, roomCode, callback) => {
        return db.query("CALL PROC_CREATE_DUAL_ROOM(?, ?, ?)", [userName, userName2, roomCode], callback);
    },
    joinExistRoom : (roomCode, userName, password, callback) =>{
        return db.query("CALL PROC_JOIN_ROOM(?, ?, ?);", [roomCode, userName, password], callback);
    },
    leaveRoom : (roomCode, userName, callback) =>{
        return db.query("");
    }  
};

module.exports = room;