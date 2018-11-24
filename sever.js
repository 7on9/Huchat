var express = require("express");
var app = express();
//app.use(express.static("api"));
var db = require("./database");
var sever = require("http").Server(app);
var io = require("socket.io").listen(sever);
//var routes = require("./routes");
var user = require("./user");
var account = require("./account");


io.on("connection", function (socket) {
	console.log("One user connected" + socket.id);

	socket.on("checkExistUserName", function (user_name) {
		account.checkExistUserName(user_name, function (err, rows) {
			if (err) {
				console.log(err);
			}
			else {
				if (rows.length > 0) {
					console.log("Ok");
				}
				else {
					console.log("Nazz");
				}
			}
		})
	});
	
	socket.on("disconnect", function () {
		console.log("One user Disconnected " + socket.id);
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


