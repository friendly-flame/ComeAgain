var fs = require('fs');
var mysql = require('mysql');
var ip = require('ip');
var secret = getConfig('../secret.json');

// Database setup

var connectionPool = mysql.createPool(secret.mysqlConnection);

// GET

exports.getPrivate = function(req, res) {
    //TODO: For added security, check IP of client
    var publicIP = req.params.publicIP;
    connectionPool.getConnection(function(err, connection) {
        if (err) {
            console.error('CONNECTION error: ', err);
            res.statusCode = 503;
            res.json({
                result: 'error',
                error: err.code
            });
        } else {
            connection.query('SELECT * FROM games WHERE public_ip='+publicIP+" ORDER BY last_updated DESC",
                function(err, rows) {
                    if (err) {
                        console.error(err);
                        res.statusCode = 500;
                        res.json({
                            result: 'error',
                            error: err.code
                        })
                    } else {
                        if (rows.length == 0) {
                            err = 'Could not find requested IP';
                            console.error(err);
                            res.statusCode = 404;
                            res.json({
                                result: 'error',
                                error: err
                            })
                        } else {
                            //TODO: Account for multiple valid games on a single subnet
                            res.json({
                                result: rows[0],
                                error: ''
                            })
                        }
                    }
                    connection.release();
                });
        }
    });
};

// POST

exports.postPrivate = function(req, res) {
    //TODO: For added security, ensure client IP matches publicIP
    if ('publicIP' in req.body && 'privateIP' in req.body) {
        connectionPool.getConnection(function(err, connection) {
            if (err) {
                console.error('CONNECTION error: ', err);
                res.statusCode = 503;
                res.json({
                    result: 'error',
                    error: err.code
                });
            } else {
                connection.query('INSERT INTO games (public_ip, private_ip) VALUES ('+
                    req.body.publicIP+', '+req.body.privateIP+')',
                    function(err) {
                        if (err) {
                            console.error(err);
                            res.statusCode = 500;
                            res.json({
                                result: 'error',
                                error: err.code
                            })
                        } else {
                            res.json({
                                result: 'success',
                                error: ''
                            })
                        }
                        connection.release();
                    });
            }
        });
    } else {
        res.statusCode = 404;
        res.json({
            result: 'error',
            error: 'Invalid request'
        })
    }
};

// Utility functions

function readJsonFileSync(filepath, encoding) {

    if (typeof (encoding) == 'undefined'){
        encoding = 'utf8';
    }
    var file = fs.readFileSync(filepath, encoding);
    return JSON.parse(file);
}

function getConfig(file) {

    var filepath = __dirname + '/' + file;
    console.log(filepath);
    return readJsonFileSync(filepath);
}