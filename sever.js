var express = require("express");
var app = express();
var sever = require("http").Server(app);
var io = require("socket.io").listen(sever);
//var routes = require("./routes");
var user = require("./user");
var account = require("./account");
var db = require("./database");
var mail = require("./mail");
var fs = require("fs");

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
	socket.on("clientSendImage", function (data) {
		console.log("SERVER SAVED A NEW IMAGE");
		var filename = getFilenameImage(socket.id);
		arrayImage.push(filename);
		fs.writeFile(filename, data);
	});
	socket.on('clientSendRequestImage', function (userName, data) {
		// let dir = "img/" + "" + userName + "" + ".png";
		let index = arrayImage.indexOf(userName);
		let filename = index == -1 ? "" : arrayImage[index];
		fs.readFile(filename, function (err, data) {
			if (!err) {
				socket.emit('severSendImage', data);
				//socket.emit("result", "clientSendRequestImage",true);
				console.log("SEND TO CLIENT A FILE: " + filename);
			} else {
				socket.emit("result", "clientSendRequestImage", false);
				console.log('THAT BAI: ' + filename);
			}
		});
	});
	socket.on("register", async (userName, password, email) => {
		//console.log(password);
		let existUserName = await account.checkExistUserName(userName);
		if (existUserName) {
			socket.emit("result", "existUserName", true);
			return;
		}
		let existEmail = await account.checkExistEmail(email);
		if (existEmail) {
			socket.emit("result", "existEmail", true);
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
		if (userName == "admin") socket.emit("result", "login", true);
		else
			account.login(userName, password, (err, rows) => {
				if (err) {
					console.log(err);
				}
				else {
					if (rows.length > 0) {
						socket.emit("result", "login", true);
						socket.id = rows[0].userName;
						console.log(socket.id);
					}
					else {
						socket.emit("result", "login", false);
					}
				}
			})
	});

	socket.on("logout", (userName) => {
		account.logout(userName, (err, rows) => {
			if (err) {
				console.log(err);
			}
			else {
				if (rows.affectedRows > 0) {
					//console.log("account created " + userName);
					socket.emit("result", "logout", true);
				}
				else {
					socket.emit("result", "logout", false);
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
		console.log("User disconnected " + socket.id);
	})
});

app.get("/", function (req, res) {
	res.sendFile(__dirname + "/index.html");
});

sever.listen(2409, () => {
	console.log("Sever is online!");
});
//app.use("/", routes);

function getFilenameImage(id) {
	return "img/" + id + ".png";
}

function getMilis() {
	var date = new Date();
	var milis = date.getTime();
	return milis;
}


module.exports = sever;


