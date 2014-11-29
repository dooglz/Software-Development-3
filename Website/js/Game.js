var aaf =0;

$(document).ready(function(){
   SOCKETSTATE = "NULL";
    verifyWSsupport();
    $("#connectbtn").click(function(){
        Connect($("#Ipbox").val());
    });
    $("#resbtn").click(function(){
        getState();
    });
   // var timer = window.requestAnimationFrame(Update);
    //window.requestAnimationFrame
    var timer = setInterval(Update, 100);
});



function verifyWSsupport(){
 if("WebSocket" in window){
    console.info("Websockets supported");
 }else{
    console.info("Websockets not supported");
    SOCKETSTATE = "NOSUPPORT";
    $("#constatus").html("Websockets not supported on this browser!");
    $("#connectbtn").attr("disabled", true);
 }

}

var SOCKET;
var SOCKETSTATE;
function Connect(IP){
    console.info("Attmepting to conenct to "+IP);
    SOCKETSTATE = "CONNECTING";
    try{
        SOCKET = new WebSocket(IP);
    } catch (e) {
        console.error('Sorry, the web socket at "%s" is un-available', IP);
        $("#constatus").html("Can't connect to host");
        SOCKETSTATE = "NULL";
        return;
    }
    
    SOCKETSTATE = "CONNECTING";
    $("#constatus").html("Connecting...");

    SOCKET.onopen = function()
    {
        $("#constatus").html("Socket connected");
        SOCKETSTATE = "CONNECTED";
        SOCKET.send("Hello");
    };
    
    SOCKET.onmessage = function (evt) 
    { 
        var received_msg = evt.data;
        Decoder(received_msg);
    };
    
    SOCKET.onclose = function()
    { 
        // websocket is closed.
        console.info("Connection is closed..."); 
        $("#constatus").html("Disconnected");
        SOCKETSTATE = "NULL";
    };
    
    SOCKET.onerror= function()
    { 
        // websocket is closed.
        console.info("Connection error"); 
    };
    
}

function Decoder(msg)
{
 //   var ship = {type:"frigate", X:0, Y:3};
 //   var gamegrid = [{type:"frigate", X:0, Y:3},{type:"frigate", X:2, Y:3},{type:"player", X:4, Y:3}];
 //   var state = {time:100,state:"play",grid:gamegrid} ;
   // console.info(JSON.stringify(state));
    var obj;
    
    try {
        obj = JSON.parse(msg);
    } catch (e) {
        // is not a valid JSON string
        switch(msg)
        {
            case("Hello"):
                console.info("Server says Hello");
                $("#constatus").html("Server connected");
                SOCKETSTATE = "READY";
                break;
            default:
                console.info("Unkown message from host: "+msg);
                break;
        }
        return;
    }
   
    if(obj.type == "state"){
        console.info("state update, "+obj.time);
        UpdateGrid(obj.grid);
    }
}

function getState(){
    if(SOCKETSTATE == "READY")
    {
        SOCKET.send("state");
    }   
}

var ships = [];

function UpdateGrid(grid){
    console.info(grid);
    for	(i = 0; i < grid.length; i++) {
        
        if(grid[i].id == undefined){console.error("!!!!");}
        
        var result = $.grep(ships, function(e){ return e.id == grid[i].id});
        
        if (result.length == 0) {
          // not found, new ship
            var newship = new Object();
            
            newship.X = grid[i].X;
            newship.Y = grid[i].Y;
            newship.type = grid[i].type;
            newship.id = grid[i].id;
        
            newship.jqo = jQuery('<div/>', {
                id: 'ship'+newship.id,
                class: 'ship red',
            }).appendTo('#'+newship.X+newship.Y);
        
            ships[ships.length] = newship;
        
        } else if (result.length == 1) {
            result[0].X = grid[i].X;
            result[0].Y = grid[i].Y;
        } else {
            console.error("!!!!2");
        }
    }
    
}


function Update(yo){
    aaf += 0.1;
    var os = $('td').outerWidth();
    $( ".gametile" ).each(function( index ) {
       
        //foreach gametile
        var s = $(this).children('.ship').size();
        $(this).children('.ship').each(function( index2 ) {
            var is = $(this).outerWidth();
            var max = (os - is)*0.5;
            console.log(os +" "+is);
          //  console.log( index +"- "+ index2 +": " + $( this ).attr("class") );
            var p = Math.PI/s;
            var q = aaf - (p * (index2+1));
            var x = max*Math.sin(q);
            var y = max*Math.cos(q);
            // console.log(p+" "+q+" "+x);
            $( this ).css("left",(max+x)+"px");
            $( this ).css("top",(max+y)+"px");
        });
    });
}



//-------------------------------

//var ship = {id:0,type:"frigate", X:0, Y:3, jqo};






















