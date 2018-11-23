var express = require("express");
var app = express();
//app.use(express.static("api"));
var db = require("./database");
var sever = require("http").Server(app);
var io = require("socket.io").listen(sever);
//var routes = require("./routes");

io.on("connection", function(socket){
  console.log("One user connected" + socket.id);
  socket.on("disconnect", function(){
    console.log("One user Disconnected " + socket.id);
  })
});

app.get("/", function(req, res){
    res.sendFile(__dirname+"/index.html");
});

sever.listen(2409, function(){
  console.log("Sever is online!");
});
//app.use("/", routes);


module.exports = sever;

