var express = require("express");
var app = express();
var sever = require("http").Server(app);
var io = require("socket.io").listen(sever);
var fs = require("fs");

//var routes = require("./routes");
//impliment modules
var user = require("./user");
var account = require("./account");
var db = require("./database");
var mail = require("./mail");
var room = require("./room");


var arrayImage = new Array();

fs.readdir("img/", function (err, files) {
	if (err) {
		return;
	}
	files.forEach(function (f) {
		arrayImage.push("img/" + f);
	});
});

io.on("connection", function (socket) {
	socket.emit("result", "connection", true);
	console.log("One user connected " + socket.id);
	// socket.on("user",async (userName) => {
	// 	let check = await account.checkExistUserName(userName);
	// 	console.log("have account -> ", check);
	// // });
	// socket.on("recoveryPassword", (userName, email) =>{
	// 	mail.sendMail(u)
	// });
	// mail.sendMail("tamdaulong207@gmail.com");
	socket.on("clientSendImageUser", function (data) {
		try {
			console.log("SERVER SAVED A NEW IMAGE");
			var filename = getFilenameImageUser(socket.id);
			arrayImage.push(filename);
			fs.writeFile(filename, data);
			socket.emit("result", "clientSendImageUser", true);
		} catch (ex) {
			socket.emit("result", "clientSendImageUser", false);
		}
	});

	socket.on("clientSendImageRoom", function (roomCode,data) {
		try {
			console.log("SERVER SAVED A NEW IMAGE");
			var filename = getFilenameImageRoom(roomCode);
			arrayImage.push(filename);
			fs.writeFile(filename, data);
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
				socket.emit("result",'severSendImageRoom', true, data);
				//socket.emit("result", "clientSendRequestImage",true);
				console.log("SEND TO CLIENT A FILE: " + filename);
			} else {
				socket.emit("result",'severSendImageRoom', false);
				console.log('THAT BAI: ' + filename);
			}
		});
	});

	// console.log(arrayImage);
	socket.on('clientRequestImageUser', function (userName) {
		let dir = getFilenameImageUser(userName);
		let index = arrayImage.indexOf(dir);
		let filename = index == -1 ? "" : arrayImage[index];
		fs.readFile(filename, function (err, data) {
			if (!err) {
				socket.emit("result",'severSendImageUser', true, data);
				//socket.emit("result", "clientSendRequestImage",true);
				console.log("SEND TO CLIENT A FILE: " + filename);
			} else {
				socket.emit("result",'severSendImageUser', false);
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
		account.registerAccount(userName, password, email, (err, rows) => {
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
					socket.id = rows[0][0].USER_NAME;
					console.log("User " +socket.id+ " connected!");
				}
				else {
					//console.log("emited");
					socket.emit("result", "login", false);
				}
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
					console.log("account "+ socket.id +" logout");
					//console.log("account created " + userName);
					socket.emit("result", "logout", true);
				}
				else {
					socket.emit("result", "logout", false);
				}
			}
		})
	});

	socket.on("clientRequestListRoom", (userName) => {
		room.getListRoomOfUser(userName, (err, rows) => {
			if (err) {
				socket.emit("result", "clientRequestListRoom", false);
			}
			else
			{	
				socket.emit("result", "clientRequestListRoom", true, rows[0]);
				console.log(rows[0]);
				for(i in rows[0]){
					console.log(rows[0][i].ROOM_CODE);
					socket.join(rows[0][i].ROOM_CODE);
				}
			}
		})
	});
	//chat
	socket.on("joinRoom", (room) => {
		//find room if exist
		socket.join(room);
	});

	socket.on("leaveRoom", (room) => {
		//remove from list room
		socket.leave(room);
	});

	socket.on("clientGetHistoryChatRoom", (room) => {
		//do some fucking thing sever
		socket.emit("severReturnHistoryChatRoom");
	});

	socket.on("messageFromClient", async (room, message) => {
		io.to(room).emit("messageFromSever", message);
		//console.log(message);
	});

	socket.on("disconnect", () => {
		console.log("User disconnected!");
	})
});

app.get("/", function (req, res) {
	res.sendFile(__dirname + "/index.html");
});

sever.listen(2409, () => {
	console.log("Sever is online!");
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

function getMilis() {
	var date = new Date();
	var milis = date.getTime();
	return milis;
}


module.exports = sever;


