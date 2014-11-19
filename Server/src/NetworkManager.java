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
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		//Handle client received message here
	    //send a message back:
	    conn.send("A message");
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		//Handle error during transport here
	}

}