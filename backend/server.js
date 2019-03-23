var express = require("express");
var app = express();
var server = require("http").Server(app);
var io = require("socket.io").listen(server);
var fs = require("fs");

//var routes = require("./routes");
//impliment modules
var user = require("./user");
var account = require("./account");
var db = require("./config/database");
var mail = require("./mail");
var room = require("./room");


var arrayImage = [];
var recoveryCode = [];
var socketUser = new Map();
fs.readdir("img/", function (err, files) {
	if (err) {
		return;
	}
	files.forEach(function (f) {
		arrayImage.push("img/" + f);
	});
});

io.on("connection", function (socket) {
	var roomOfUser = [];
	socket.leave(socket.id);
	socket.emit("result", "connection", true);
	console.log("One user connected " + socket.id);
	// socket.on("user",async (userName) => {
	// 	let check = await account.checkExistUserName(userName);
	// 	console.log("have account -> ", check);
	// // });
	
	
	socket.on("recoveryPassword",async (userName, email) =>{
		let existUserName = await account.checkExistUserName(userName);
		if (!existUserName) {
			socket.emit("result", "recoveryPassword", false);
			return;
		}
		let existEmail = await account.checkExistEmail(email);
		if (!existEmail) {
			socket.emit("result", "recoveryPassword", false);
			return;
		}

		let code = ((getMilis() * 2 * 3.14*3.14)*100).toString().substr(9, 6);

		recoveryCode.push({
			"key" : userName,
			"code" : code
		});
		
		mail.sendMail();		

	});
	
	socket.on("setAvatar", function (data) {
		try {
			console.log("SERVER SAVED A NEW IMAGE");
			var filename = getFilenameImageUser(socket.userName);
			arrayImage.push(filename);
			console.log(filename);
			fs.writeFileSync(filename, data);
	
			socket.emit("result", "setAvatar", true);
		} catch (ex) {
			console.log(ex);
			
			socket.emit("result", "setAvatar", false);
		}
	});

	socket.on("clientSendImageRoom", function (roomCode, data) {
		try {
			console.log("SERVER SAVED A NEW IMAGE");
			var filename = getFilenameImageRoom(roomCode);
			arrayImage.push(filename);
			fs.writeFileSync(filename, data,);
			socket.emit("result", "clientSendImageRoom", true);
		} catch (ex) {
			socket.emit("result", "clientSendImageRoom", false);
		}
	});

	socket.on("clientRequestImageRoom", function (roomCode) {
		let dir = getFilenameImageRoom(roomCode);
		let index = arrayImage.indexOf(dir);
		let filename = index == -1 ? "" : arrayImage[index];
		fs.readFile(filename, function (err, data) {
			if (!err) {
				//console.log(data.toJSON(data));
				socket.emit("result", 'serverSendImageRoom', true, data, roomCode);
				//socket.emit("result", "clientSendRequestImage",true);
				console.log("SEND TO CLIENT A FILE: " + filename);
			} else {
				// console.log(filename);
				socket.emit("result", 'serverSendImageRoom', false);
				console.log('THAT BAI:' + err + " " + filename);
			}
		});
	});

	// console.log(arrayImage);

	socket.on('clientRequestImageUser', function (userName) {
		let dir = getFilenameImageUser(userName);
		let index = arrayImage.indexOf(dir);
		let filename = index == -1 ? "img/default.png" : arrayImage[index];
		fs.readFile(filename, function (err, data) {
			if (!err) {
				socket.emit("result", 'serverSendImageUser', true, data, userName);
				//socket.emit("result", "clientSendRequestImage",true);
				console.log("SEND TO CLIENT A FILE: " + filename);
			} else {
				socket.emit("result", 'serverSendImageUser', false);
				console.log('THAT BAI: ' + filename);
			}
		});
	});

	socket.on("register", async (userName, password, email) => {
		//console.log(password);
		let existUserName = await account.checkExistUserName(userName);
		if (existUserName) {
			socket.emit("result", "register", false);
			return;
		}
		let existEmail = await account.checkExistEmail(email);
		if (existEmail) {
			socket.emit("result", "register", false);
			return;
		}
		account.registerAccount(userName.toLowerCase(), password, email.toLowerCase(), (err, rows) => {
			if (err) {
				console.log(err);
				throw err;
			}
			else {
				if (rows.affectedRows > 0) {
					//console.log("account created " + userName);
					socket.emit("result", "register", true);
				}
				else {
					socket.emit("result", "register", false);
				}
			}
		})
	});

	socket.on("login", (userName, password) => {
		// if (userName == "admin") socket.emit("result", "login", true);
		// else
		account.login(userName, password, (err, rows) => {
			if (err) {
				console.log(err);
				socket.emit("result", "login", false);
			}
			else {
				if (rows.length > 0) {
					socket.emit("result", "login", true, rows[0][0]);
					socket.userName = rows[0][0].USER_NAME;
					// socketUser.set()
					socket.join(userName);
			 		console.log("User " + socket.userName + " login!");
				}
				else {
					//console.log("emited");
					socket.emit("result", "login", false);
				}
			}
		})
	});

	socket.on("clientRequestPublicInfoUser", () => {
		let returnPack;
		let listImg = [];
		user.getPublicInfoUser(async (err, rows) => {
			if (err) {
				console.log(err);
			}
			else {
				socket.emit("serverSendListPublicInfoUser" ,rows[0]);
				/*
				// let p = Promise.resolve()
				// .then(async () => {
				// 	console.log("1");
				// 	let newRows = [];
				// 	for(let i = 0; i < rows[0].length; i++){
				// 		newRows.push(rows[0][i]);
				// 		await getImageUser(rows[0][i].USER_NAME)
				// 			.then((data) => {
				// 				//Object.defineProperty(newRows[i], "AVATAR", {value : data});
				// 				// newRows[i]["AVATAR"] = JSON.stringify(data);
				// 				listImg.push(data);
				// 				// console.log();
				// 				//console.log(Object.getOwnPropertyNames(rows[0][i]));
				// 				// console.log(newRows[i]);
				// 			})
				// 			.catch((err) => console.error(err));
				// 	}
				// 	console.log("2");
				// 	// console.log(newRows);
				// 	return newRows;
				// })
				// .then((newRows) => {
				// 	console.log("3");
				// 	returnPack = newRows;
				// 	// console.log(Object.getOwnPropertyNames(newRows[0]));
				// })
				// .catch(err => console.log(err));
				// await p;
				// console.log("4");
				// // console.log(listImg);
				socket.emit("serverSendListPublicInfoUser", returnPack);
				// console.log(Object.getOwnPropertyNames(rows[0][i]));
				// Object.defineProperty(rows[0][i], "AVATAR", {value : data});
			}
			*/
		}
		})
	});

	socket.on("logout", (userName) => {
		account.logout(userName, (err, rows) => {
			if (err) {
				console.log(err);
				socket.emit("result", "logout", false);
			}
			else {
				if (rows.affectedRows > 0) {
					console.log("account " + userName + " logout");
					//console.log("account created " + userName);
					socket.emit("result", "logout", true);
					for (let i in roomOfUser) {
						socket.leave(i);
					}
					roomOfUser = [];
				}
				else {
					socket.emit("result", "logout", false);
				}
				// socket.disconnect();
				// socket.removeAllListeners();
			}
		})
	});
	
	socket.on("clientRequestListRoom", () => {
		room.getAllListRoom((err, rows) => {
			if (err) {
				console.log(err);
			}
			else {
				socket.emit("serverSendListRoom", rows[0]);
				// console.log(rows[0]);
				for (i in rows[0]) {
					// console.log(socket);
					roomOfUser.push(rows[0][i].ROOM_CODE);
					socket.join(rows[0][i].ROOM_CODE);
					// console.log(socket.adapter.rooms);
				}
			}
		//	console.log(socket.adapter.rooms);
		})
	});

	socket.on("clientRequestListRoomOfUser", (userName) => {
		room.getListRoomOfUser(userName, (err, rows) => {
			if (err) {
				console.log(err);
				socket.emit("result", "serverSendListRoomOfUser", false);
			}
			else {
				socket.emit("result", "serverSendListRoomOfUser", true, rows[0]);
				// console.log(rows[0]);
				for (i in rows[0]) {
					// console.log(socket);
					roomOfUser.push(rows[0][i].ROOM_CODE);
					socket.join(rows[0][i].ROOM_CODE);
					// console.log(socket.adapter.rooms);
				}
			}
		//	console.log(socket.adapter.rooms);
		})
	});

	socket.on("clientRequestListMemberOfRoom", (roomCode) => {
		room.getListMemberOfRoom(roomCode, (err, rows) => {
			if(!err){
				socket.emit("result", "serverSendListMemberOfRoom", true, roomCode, rows[0]);
			}
			else console.log(err);
		});
	});

	//chat

	socket.on("checkExistRoom", (roomCode) => {
		let res = room.checkExistRoom(roomCode);
		if(res){
			socket.emit("result", "checkExistRoom", true);
		}else{
			socket.emit("result", "checkExistRoom", false);
		}
	});

	socket.on("createRoom", (pack) => {
		pack.roomCode = getMilis() + "" + pack.userName;
		try {
			console.log("SERVER SAVED A NEW IMAGE");
			var filename = getFilenameImageRoom(pack.roomCode);
			arrayImage.push(filename);
			fs.writeFile(filename, pack.imgRoom);
		} catch (ex) {
			console.log(ex);
		}
		console.log(pack);
		room.createRoom(pack, (err, rows) => {
			if(err){
				console.log(err);
				socket.emit("result","createRoom", false);
			}else{
				if(!pack.isPrivate){
					io.emit("newPublicRoom", pack);
					io.emit("serverSendImageRoom", pack.imgRoom, pack.roomCode);
				}
				socket.emit("result", "createRoom", true, pack.roomCode);
			}
		});
	});

	socket.on("joinRoom", (roomCode) => {
		socket.join(roomCode);
	});

	socket.on("joinExistRoom", async (roomCode, userName, password) => {
		let isExist =  await room.checkExistRoom(roomCode);
		if(!isExist){
			socket.emit("result", "joinExistRoom", false, "roomNotExist");
		}
		room.joinExistRoom(roomCode, userName, password, (err, rows) => {
			if(err){
				socket.emit("result", "joinExistRoom", false, "wrongPassword");
			}else{
				console.log(rows);
				room.getPublicInfoOfRoom(roomCode, (err,rows)=>{
					if(err){
						console.log(err);
					}else{
						console.log(rows[0]);
						// socket.emit("result", "newRoom", true, rows[0][0]);
						socket.emit("result", "joinExistRoom", true);
						socket.join(roomCode);
					}
				});
				
			}	
		});
	});

	socket.on("joinDualRoom", async (userName, userName2) => {
		let roomCode = (userName.toLowerCase() < userName2.toLowerCase()) ? 
						userName.toLowerCase().concat("#" , userName2.toLowerCase()) :
						userName2.toLowerCase().concat("#" , userName.toLowerCase());
		let isExist =  await room.checkExistRoom(roomCode);
		console.log(isExist);
		if(!isExist){
			room.createDualRoom(userName, userName2, roomCode, (err, rows) => {
					if(err || rows.affectedRows == 0){
						console.log(err);
					}else{
						room.getPublicInfoOfRoom(roomCode, (err,rows)=>{
							if(err){
								console.log(err);
							}else{
								console.log(rows[0]);
								io.to(socket.userName == userName2 ? userName : userName2).emit("result", "newRoom", true, rows[0][0]);
								socket.emit("result", "newRoom", true, rows[0][0]);
							}
						});
					}
				});
			}
		// 	}
		// 	else{
		// 		room.createDualRoom(userName2, userName, roomCode, (err, rows) => {
		// 			if(err || rows.affectedRows == 0){
		// 				console.log(err);
		// 			}else{
		// 				room.getPublicInfoOfRoom(roomCode, (err,rows)=>{
		// 					if(!err)
		// 						socket.emit("newRoom", rows[0]);
		// 				});
		// 			}
		// 		});
		socket.join(roomCode);
	});

	socket.on("leaveRoom", (roomCode) => {
		//remove from list room
		socket.leave(roomCode);
		socket.removeListener(clearInterval);
	});

	socket.on("clientRequestHistoryChatRoom", (roomCode) => {
		room.getHistoryOfChatRoom(roomCode, (err, rows) => {
			if (!err && rows.length > 0) {
				// console.log(rows[0]);
				socket.emit("result", "serverSendHistoryChatRoom", true, rows[0], roomCode);
			}
			else {
				socket.emit("result", "serverSendHistoryChatRoom", false);
			}
		})
	});

	socket.on("clientSendMessage", (roomCode, userName, content) => {
		// console.log(roomCode, userName, content);
		socket.join(roomCode);
		// console.log(socket.adapter.rooms); 
		room.userChat(roomCode, userName, content, () => {
			io.to(roomCode).emit("serverSendMessage", roomCode, userName, content);
			// socket.broadcast.to(roomCode).emit("serverSendMessage",roomCode, userName, content);
			// io.sockets.to(roomCode).emit("serverSendMessage",roomCode, userName, content);
		});
	});

	//edit profile

	socket.on("changePassword", (userName, password) => {
		account.changePassword(userName, password, (err, rows) =>{
			if(err){
				console.log(err);
			}else{
				if(rows.affectedRows > 0){
					socket.emit("result", "changePassword", true);
				}
				else {
					socket.emit("result", "changePassword", false);
				}
			}
		})
	});

	socket.on("changeFullName", (userName, fullName) => {
		account.changeFullName(userName, fullName, (err, rows) =>{
			if(err){
				console.log(err);
			}else{
				if(rows.affectedRows > 0){
					socket.emit("result", "changeFullName", true);
				}
				else {
					socket.emit("result", "changeFullName", false);
				}
			}
		})
	});

	socket.on("changeMail", async (userName, mail) => {
		let existEmail = await account.checkExistEmail(mail);
		if (existEmail) {
			socket.emit("result", "changeMail", false, "duplicate");
			return;
		}
		account.changeMail(userName, mail, (err, rows) =>{
			if(err){
				socket.emit("result", "changeMail", false, err);
				console.log(err);
			}else{
				if(rows.affectedRows > 0){
					socket.emit("result", "changeMail", true);
				}
				else {
					socket.emit("result", "changeMail", false, "sever error");
				}
			}
		});
	});

	socket.on("changePhone", (userName, phone) => {
		account.changePhone(userName, phone, (err, rows) =>{
			if(err){
				console.log(err);
			}else{
				if(rows.affectedRows > 0){
					socket.emit("result", "changePhone", true);
				}
				else {
					socket.emit("result", "changePhone", false);
				}
			}
		})
	});

	socket.on("changeGender", (userName, gender) => {
		account.changeGender(userName, gender, (err, rows) =>{
			if(err){
				console.log(err);
			}else{
				if(rows.affectedRows > 0){
					socket.emit("result", "changeGender", true);
				}
				else {
					socket.emit("result", "changeGender", false);
				}
			}
		})
	});

	socket.on("changeDob", (userName, dob) => {
		account.changeDoB(userName, dob, (err, rows) =>{
			if(err){
				console.log(err);
			}else{
				if(rows.affectedRows > 0){
					socket.emit("result", "changeDob", true);
				}
				else {
					socket.emit("result", "changeDob", false);
				}
			}
		})
	});

	socket.on("disconnect", () => {
		console.log("User disconnected!");
		for (let i in roomOfUser) {
			socket.leave(i);
		}
		roomOfUser = [];
		// console.log(socket.adapter.rooms);
	})
});

app.get("/", function (req, res) {
	res.sendFile(__dirname + "/index.html");
});

server.listen(2409, () => {
	console.log("Server is online!");
});
//app.use("/", routes);

function getFilenameImageUser(id) {
	let name = "user" + "" + id.toLowerCase();
	return "img/" + name + ".png";
}

function getFilenameImageRoom(id) {
	let name = "room" + "" + id.toLowerCase();
	return "img/" + name + ".png";
}

var getImageUser = (userName) => {
	return new Promise(function (resolve, reject) {
		let dir = getFilenameImageUser(userName);
		let index = arrayImage.indexOf(dir);
		let filename = index == -1 ? "img/default.png" : arrayImage[index];
		fs.readFile(filename, function (err, data) {
			if (err) {
				reject(err);
			}
			// console.log("2.1");
			resolve(data);
		});
	})
};
function getMilis() {
	var date = new Date();
	var milis = date.getTime();
	return milis;
}

module.exports = server;
