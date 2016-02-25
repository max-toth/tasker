var express = require("express");
var bodyParser = require("body-parser");
var assert = require('assert');
var app = express();
var mongo = require('mongodb');
var monk = require('monk');
var db = monk('localhost:27017/tasker-db');

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use(function(req,res,next){
    req.db = db;
    next();
});

var routes = require("./routes/routes.js")(app);

var server = app.listen(3000, function () {
    console.log("Listening on port %s...", server.address().port);
});