/*jslint browser: true, devel: true */
"use strict";
var aaf = 0;
var SOCKET;
var SOCKETSTATE;
var ANIMATING;
var GAMESTATE;
var ships = [];
var PLAYEROBJ;
var MOVETIME = 1500;

function verifyWSsupport() {
    if ("WebSocket" in window) {
        console.info("Websockets supported");
    } else {
        console.info("Websockets not supported");
        SOCKETSTATE = "NOSUPPORT";
        $("#constatus").html("Websockets not supported on this browser!");
    }
}

function updateUI()
{
	if(ANIMATING){
		$("#myonoffswitch").attr("disabled", true);
		$("#trnbtn").attr("disabled", true);
		$("#resbtn").attr("disabled", true);
		$("#undobtn").attr("disabled", true);
		$("#redobtn").attr("disabled", true);
		$("#Ipbox").attr("disabled", true);
		$("#connectbtn").attr("disabled", true);
		$("#disconctbtn").attr("disabled", true);
		return;
	}
	
	switch(SOCKETSTATE)
	{
		case("NOSUPPORT"):
			$("#myonoffswitch").attr("disabled", true);
			$("#trnbtn").attr("disabled", true);
			$("#resbtn").attr("disabled", true);
			$("#undobtn").attr("disabled", true);
			$("#redobtn").attr("disabled", true);
			$("#Ipbox").attr("disabled", true);
			$("#connectbtn").attr("disabled", true);
			$("#disconctbtn").attr("disabled", true);
			break;
		case("NULL"):
			$("#myonoffswitch").attr("disabled", true);
			$("#trnbtn").attr("disabled", true);
			$("#resbtn").attr("disabled", true);
			$("#undobtn").attr("disabled", true);
			$("#redobtn").attr("disabled", true);
			$("#Ipbox").attr("disabled", false);
			$("#connectbtn").attr("disabled", false);
			$("#disconctbtn").attr("disabled", true);
			break;
		case("READY"):
			switch(GAMESTATE)
			{
				case("GOTSTATE"):
					$("#myonoffswitch").attr("disabled", false);
					$("#trnbtn").attr("disabled", false);
					$("#resbtn").attr("disabled", false);
					$("#undobtn").attr("disabled", false);
					$("#redobtn").attr("disabled", false);
					$("#Ipbox").attr("disabled", true);
					$("#connectbtn").attr("disabled", true);
					$("#disconctbtn").attr("disabled", false);
					break;
				case("ANIMATE"):
					$("#myonoffswitch").attr("disabled", true);
					$("#trnbtn").attr("disabled", true);
					$("#resbtn").attr("disabled", true);
					$("#undobtn").attr("disabled", true);
					$("#redobtn").attr("disabled", true);
					$("#Ipbox").attr("disabled", true);
					$("#connectbtn").attr("disabled", true);
					$("#disconctbtn").attr("disabled", true);
					break;
				default:
					$("#myonoffswitch").attr("disabled", false);
					$("#trnbtn").attr("disabled", true);
					$("#resbtn").attr("disabled", false);
					$("#undobtn").attr("disabled", true);
					$("#redobtn").attr("disabled", true);
					$("#Ipbox").attr("disabled", true);
					$("#connectbtn").attr("disabled", true);
					$("#disconctbtn").attr("disabled", false);
					break;
			}
			break;
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
			case ("GAMEOVER"):
				console.info("Server says GAMEOVER");
				GAMESTATE = "OVER";
			break;
		default:
			console.info("Unkown message from host: " + msg);
			break;
		}
		return;
	}

	if (obj.type === "state") {
		GAMESTATE = "GOTSTATE";
		console.info("state update, " + obj.time);
		if(obj.state == "over")
		{
			 gameOver();
		}
		UpdateGrid(obj.grid);
	}
}

function gameOver()
{
	console.warn("Game over!");
	alert("Game over!");
	GAMESTATE = "OVER"
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
		GAMESTATE = "NULL";
		updateUI();
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
	GAMESTATE = "NULL";
    verifyWSsupport();
    $("#connectbtn").click(function () {
        Connect($("#Ipbox").val());
    });
    $("#disconctbtn").click(function () {
		SOCKET.close();
    });
	$("#resbtn").click(function () {
		Restart();
    });
	$("#trnbtn").click(function () {
       doTurn();
       ANIMATING = true;
		updateUI();
       var wait = setInterval(function(){
            if( $("div:animated").size() <=0 ){
                clearInterval(wait);
				ANIMATING = false;
            }
        },200);
    });
	
	$("#myonoffswitch").click(function () {
        if($("#myonoffswitch").prop('checked')){
			PLAYEROBJ.jqo.css('background-image','url("img/shipP.gif")');
		}else{
 			PLAYEROBJ.jqo.css('background-image','url("img/shipA.gif")');
		}
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

function Restart(){
	$(".ship").remove();
	GAMESTATE = "NULL";
	ships = [];
	PLAYEROBJ = null;
	if (SOCKETSTATE === "READY") {
        SOCKET.send("RESTART");
    }
	getState();
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
                    col = 'p';
                    break;
                default:
                    col = 'e1';
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
                result[0].jqo.animate({left: newX+ "px", top: newY+ "px"},MOVETIME);
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
		console.error("player object data not received in state update!");
	}else{
		console.error("dead ship");
		//move towards player and explode
		ships.splice(index,1);
		
        var playertile = tileToCoord(PLAYEROBJ.X,PLAYEROBJ.Y);
        var newX = getRandomInt(playertile.minX,playertile.maxX);
        var newY = getRandomInt(playertile.minY,playertile.maxY);
        deadship.jqo.animate({left: newX+ "px", top: newY+ "px"},MOVETIME)
        .animate({opacity:0},1500, function() {deadship.jqo.remove();});
	}

}


function Update(yo) {
	updateUI();
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