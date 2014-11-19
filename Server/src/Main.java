import java.util.ArrayList;
import java.util.HashMap;


public final class Main {
	private static HashMap<String, Game> _games;
	private static NetworkManager _netMan;
	
	public static void main(String[] args) {
		System.out.println("SD# course work - Sam Serrels");
		_netMan = new NetworkManager();
		_netMan.start();
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
		}
		
	}
	
	public void newGame(String name)
	{
		_games.put(name, new Game());
	}
	
	public void removeGame(String name)
	{
		_games.remove(name);
	}
}
