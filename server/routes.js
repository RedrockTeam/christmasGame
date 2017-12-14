// var db = require('./services/dbService');
var http = require('http');

module.exports = function(app, dirPath) {
    app.get('/', function(req, res) {
        res.sendFile(dirPath + '/src/views/start.html');
    })
    app.get('/index', function(req, res) {
        res.sendFile(dirPath + '/src/views/index.html');
    })
}