/*jslint browser: true, devel: true */
"use strict";
var aaf = 0;
var SOCKET;
var SOCKETSTATE;
var ships = [];
var PLAYEROBJ;

function verifyWSsupport() {
    if ("WebSocket" in window) {
        console.info("Websockets supported");
    } else {
        console.info("Websockets not supported");
        SOCKETSTATE = "NOSUPPORT";
        $("#constatus").html("Websockets not supported on this browser!");
        $("#connectbtn").attr("disabled", true);
    }
}

function Decoder(msg) {
	var obj;

	try {
		obj = JSON.parse(msg);
	} catch (e) {
		// is not a valid JSON string
		switch (msg) {
		case ("Hello"):
			console.info("Server says Hello");
			$("#constatus").html("Server connected");
			SOCKETSTATE = "READY";
			break;
		default:
			console.info("Unkown message from host: " + msg);
			break;
		}
		return;
	}

	if (obj.type === "state") {
		console.info("state update, " + obj.time);
		UpdateGrid(obj.grid);
	}
}

function Connect(IP) {
    console.info("Attmepting to conenct to " + IP);
    SOCKETSTATE = "CONNECTING";
    try {
        SOCKET = new WebSocket(IP);
    } catch (e) {
        console.error('Sorry, the web socket at "%s" is un-available', IP);
        $("#constatus").html("Can't connect to host");
        SOCKETSTATE = "NULL";
        return;
    }

    SOCKETSTATE = "CONNECTING";
    $("#constatus").html("Connecting...");

    SOCKET.onopen = function () {
        $("#constatus").html("Socket connected");
        SOCKETSTATE = "CONNECTED";
        SOCKET.send("Hello");
    };

    SOCKET.onmessage = function (evt) {
        var received_msg = evt.data;
        Decoder(received_msg);
    };

    SOCKET.onclose = function () {
        // websocket is closed.
        console.info("Connection is closed...");
        $("#constatus").html("Disconnected");
        SOCKETSTATE = "NULL";
    };

    SOCKET.onerror = function () {
        // websocket is closed.
        console.info("Connection error");
    };
}


function doTurn(){
	if (SOCKETSTATE === "READY") {
		if($("#myonoffswitch").prop('checked')){
			SOCKET.send("TurnP");
		}else{
 			SOCKET.send("TurnA");
		}
	}
}

$(document).ready(function () {
    SOCKETSTATE = "NULL";
    verifyWSsupport();
    $("#connectbtn").click(function () {
        Connect($("#Ipbox").val());
    });
    $("#resbtn").click(function () {
        getState();
    });
	$("#trnbtn").click(function () {
        doTurn();
        $("#trnbtn").attr("disabled", true);
        var wait = setInterval(function(){
            if( $("div:animated").size() <=0 ){
                clearInterval(wait);
                 $("#trnbtn").attr("disabled", false);
            }
        },200);
        
        
    });
	
    // var timer = window.requestAnimationFrame(Update);
    //window.requestAnimationFrame
    var timer = setInterval(Update, 100);
});

function getState() {
    if (SOCKETSTATE === "READY") {
        SOCKET.send("state");
    }
}



function UpdateGrid(grid) {
    console.info(grid);
    for (var i = 0; i < grid.length; i++) {

        if (grid[i].id == undefined) {
            console.error("!!!!");
        }

        var result = $.grep(ships, function(e) {
            return e.id == grid[i].id
        });

        if (result.length == 0) {
            // not found, new ship
            var newship = new Object();

            newship.X = grid[i].X;
            newship.Y = grid[i].Y;
            newship.type = grid[i].type;
            newship.id = grid[i].id;

            var col;
            switch (newship.type) {
                case "player":
                    col = 'green';
                    break;
                default:
                    col = 'red';
                    break;
            }

            newship.jqo = jQuery('<div/>', {
                id: 'ship' + newship.id,
                class: 'ship ' + col,
            }).appendTo(document.body);
			
			if(newship.type === "player"){PLAYEROBJ = newship;}
            ships[ships.length] = newship;
            
            var coords = tileToCoord(newship.X,newship.Y);
            var newX = getRandomInt(coords.minX,coords.maxX);
            var newY = getRandomInt(coords.minY,coords.maxY);
            newship.jqo.css("left", newX + "px");
            newship.jqo.css("top", newY  + "px");

        }
        else if (result.length == 1) 
        {
            var a = false;
            if(result[0].X != grid[i].X){
                result[0].X = grid[i].X;
                a=true;
            }
            if (result[0].Y != grid[i].Y){
                result[0].Y = grid[i].Y;
                a=true;
            }
            if(a){
                // I have moved
                var coords = tileToCoord(result[0].X,result[0].Y);
                var newX = getRandomInt(coords.minX,coords.maxX);
                var newY = getRandomInt(coords.minY,coords.maxY);
                // head to new tile
                result[0].jqo.animate({left: newX+ "px", top: newY+ "px"},3000);
            }
			
        } else {
            console.error("!!!!2");
        }	
    }
	//any dead ships?
	//do a reverse sweep to find them
	for (var i = 0; i < ships.length; i++) {
		var result = $.grep(grid, function(e) {
			return e.id == ships[i].id;
		});
		if (result.length == 0) {
			//dead
			killShip(i);
			i--;
		}
	}
}


function tileToCoord(tileX,tileY)
{
    var tilewidth = $('td').outerWidth();
    var shipwidth = $('.ship').outerWidth();
    var shipheight = $('.ship').outerHeight();
    var tile = $('#'+tileX+''+tileY);
    var tileo = tile.offset();
    
    var coords = new Object();
    coords.minX = tileo.left;
    coords.minY = tileo.top;
    coords.maxX = tileo.left + (tilewidth - shipwidth);
    coords.maxY = tileo.top + (tilewidth - shipheight);
    return coords;
}

function killShip(index){
	var deadship = ships[index];
	if(deadship.type == "player"){
		console.error("game over");
	}else{
		console.error("dead ship");
		//move towards player and explode
		ships.splice(index,1);
		
        var playertile = tileToCoord(PLAYEROBJ.X,PLAYEROBJ.Y);
        var newX = getRandomInt(playertile.minX,playertile.maxX);
        var newY = getRandomInt(playertile.minY,playertile.maxY);
        deadship.jqo.animate({left: newX+ "px", top: newY+ "px"},3000)
        .animate({opacity:0},2000, function() {deadship.jqo.remove();});
	}

}


function Update(yo) {
    aaf += 0.1;
    return;
    
    var os = $('td').outerWidth();

    $(".gametile").each(function(index) {

        //foreach gametile
        var p = Math.PI / $(this).children('.ship').size();
        $(this).children('.ship').each(function(index2) {
            var is = $(this).outerWidth();
            var io = $(this).outerHeight();
            var maxX = (os - is) * 0.5;
            var maxY = (os - io) * 0.5;
            var q = aaf - (p * (index2 + 1));
            var x = maxX * Math.sin(q);
            var y = maxY * Math.cos(q);
            $(this).css("left", (maxX + x) + "px");
            $(this).css("top", (maxY + y) + "px");
        });
    });
}

function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min + 1)) + min;
}