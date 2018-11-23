var express = require("express");
var routes = express.Router();
// var fs = require("fs");
// var user = require("./api/user/user");
// var login = require("./api/login/login");
// var style = require("./api/style/style");
// var userstyle = require("./api/userstyle/userstyle");
// var booking = require("./api/booking/booking");
// var getAround = require("./api/getAround/getAround");
//Basic
console.log("routes connected");


//Login
// routes.post("/login", function (req, res, next) {
//     login.findUser(req.body, function (err, rows) {
//         if (!err) {
//             if (rows.length > 0) {
//                 if (req.body.pass == rows[0].pass) {
//                     res.json({
//                         status: true,
//                         message: "Successfully authenticated !"
//                     })
//                 }
//             } else {
//                 res.json({
//                     status: false,
//                     message: "Fail ! Something wrong !"
//                 })
//             }
//         }
//     })
// });
//user

// routes.get("/", function (req, res, next) {
//     res.json({
//                             status: true,
//                             message: "Mail have been used !"
//                         })
// });
// routes.post("/user", function (req, res, next) {
//     user.addUser(req.body, function (err, callback) {
//         if (err) {
//             res.json(err);
//         } else {
//             if (Object.keys(callback).length > 1)
//                 res.json({
//                     status: true,
//                     message: "Successfully registered !"
//                 });
//             else {
//                 res.json({
//                     status: false,
//                     message: "Mail have been used !"
//                 })
//             }
//         }
//     });
// });
// routes.post("/user/:id", function (req, res, next) {
//     user.updateUser(req.params.id, req.body, function (err, callback) {
//         if (err) {
//             res.json(err);
//         } else {
//             if (Object.keys(callback).length > 1) {
//                 res.json({
//                     status: true,
//                     message: "Success !"
//                 })
//             }
//             else {
//                 res.json({
//                     status: false,
//                     message: "Fail !"
//                 })
//             }
//         }
//     });
// });
// //booking 
// routes.get("/booking/:id", function (req, res, next) {
//     booking.getBookingById(req.params.id, function (err, rows) {
//         if (err) {
//             res.json(err);
//         } else {
//             res.json(rows);
//         }
//     });
// });
// routes.post("/booking", function (req, res, next) {
//     booking.add_updateBooking(req.body, function (err, rows) {
//         if (!err) {
//             res.json(rows);
//         } else {
//             res.json(err);
//         }
//     });
// });
// routes.delete("/booking/:id", function (req, res, next) {
//     booking.deleteBooking(req.params.id, function (err, rows) {
//         if (!err) {
//             res.json(rows);
//         } else {
//             res.json(err);
//         }
//     });
// });
// //style
// routes.get("/style/:id", function (req, res, next) {
//     style.get(req.params.id, function (err, rows) {
//         if (err) {
//             res.json(err);
//         }
//         else {
//             res.json(rows);
//         }
//     });
// });
// routes.post("/style", function (req, res, next) {
//     style.addstyle(req.body, function (err, callback) {
//         if (err) {
//             res.json(err);
//         } else {
//             res.json(req.body);
//         }
//     });
// });
// routes.post("/style/:id", function (req, res, next) {
//     style.updatestyle(req.params.id, req.body, function (err, callback) {
//         if (err) {
//             res.json(err);
//         } else {
//             res.json(req.body);
//         }
//     });
// });
// //getAround
// routes.get("/getAround", function (req, res) {
//     getAround.getAroundThisTime(req.query, function (err, callback) {
//         if (err) {
//             //console.log(headers);
//             res.json(err);
//         } else {
//             res.json(callback);
//         }
//     })
// });
// routes.get("/test", function (req, res) {
//     res.json(req.query);
//     console.log("dsada");
// });

module.exports = routes;
