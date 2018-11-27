var express = require("express");
var app = express();
//app.use(express.static("api"));
var sever = require("http").Server(app);
var io = require("socket.io").listen(sever);
//var routes = require("./routes");
var user = require("./user");
var account = require("./account");
var db = require("./database");

io.on("connection", function (socket) {
	console.log("One user connected" + socket.id);
	
	// socket.on("user",async (user_name) => {
	// 	let check = await account.checkExistUserName(user_name);
	// 	console.log("have account -> ", check);
	// });

	socket.on("register", function (user_name, password) {
		//console.log(password);
		account.registerAccount(user_name, password, function (err, rows) {
			if (err) {
				console.log(err);
				throw err;
			}
			else {
				if (rows.affectedRows > 0) {
					//console.log("account created " + user_name);
					socket.emit("result", "register", true);
				}
				else {
					socket.emit("result", "register", false);
				}
			}
		})
	});

	socket.on("login", function (user_name, password) {
		account.login(user_name, password, function (err, rows) {
			if (err) {
				console.log(err);
				throw err;
			}
			else {
				if (rows.length > 0) {
					socket.emit("result", "login", true);
				}
				else {
					socket.emit("result", "login", false);
				}
			}
		})
	});
	socket.on("disconnect", function () {
		console.log("User disconnected " + socket.id);
	})
});

app.get("/", function (req, res) {
	res.sendFile(__dirname + "/index.html");
});

sever.listen(2409, function () {
	console.log("Sever is online!");
});
//app.use("/", routes);


module.exports = sever;


