$(document).ready(function(){
   SOCKETSTATE = "NULL";
    verifyWSsupport();
    $("#connectbtn").click(function(){
        Connect($("#Ipbox").val());
    });
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
}




