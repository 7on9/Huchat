var db = require("./database");

var  room = {
    getListRoomOfUser : (userName, callback) =>{
        return db.query("CALL PROC_GET_ROOM_OF_USER(?)", [userName], callback);
    }
};

module.exports = room;