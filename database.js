var mysql = require("mysql");
var connection = mysql.createPool({ 
    host : "localhost",
    user : "root",
    password : "coffee",
    database : "Huchat"
});

module.exports = connection;
