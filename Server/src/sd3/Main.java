package sd3;
import java.util.HashMap;


public final class Main {
	private static HashMap<Integer, Game> _games;
	private static NetworkManager _netMan;
	
	public static void main(String[] args) {
		System.out.println("SD# course work - Sam Serrels");
		_netMan = new NetworkManager();
		_netMan.start();
		_games = new HashMap<Integer, Game>();
		while(true){
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				
			}
		}
		
	}
	
	public static Game newGame(int gameID)
	{
		System.out.println("New game: ID - "+gameID);
		Game g = new Game();
		_games.put(gameID, g);
		return g;
	}
	
	public static void removeGame(int gameID)
	{
		System.out.println("Deleted game: ID - "+gameID);
		_games.remove(gameID);
	}
	
	public static Game getGame(int gameID)
	{
		Game g = _games.get(gameID);
		if(g == null){
			return newGame(gameID);
		}else{
			return g;
		}
	}
	
}
