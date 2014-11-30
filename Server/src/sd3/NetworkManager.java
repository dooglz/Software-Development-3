package sd3;
import java.net.InetSocketAddress;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;


public final class NetworkManager extends WebSocketServer {
	
	public NetworkManager()
	{
		 super(new InetSocketAddress(8887));
	}
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// //Handle new connection here
		
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		//Handle closing connection here	
		Main.newGame(System.identityHashCode(conn));
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		//Handle client received message here
	    //send a message back:
		//System.out.println("Message: "+message);
	    if(message.equals("state")){
	    	sendState(conn);
	    }else if(message.equals("Hello")){
	    	conn.send("Hello");
	    }else if(message.equals("TurnP")){
	    	Main.getGame(System.identityHashCode(conn)).turn(true);
	    	sendState(conn);
	    }else if(message.equals("TurnA")){
	    	Main.getGame(System.identityHashCode(conn)).turn(false);
	    	sendState(conn);
	    }
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		//Handle error during transport here
	}
	
	public static void sendState(WebSocket conn)
	{
		conn.send(Serializer.jsonify(Main.getGame(System.identityHashCode(conn))));
	}

}