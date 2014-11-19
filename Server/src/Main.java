import java.util.ArrayList;
import java.util.HashMap;


public class Main {
	private HashMap<String, Game> _games;
	
	public static void main(String[] args) {
		System.out.println("SD# course work - Sam Serrels");
		
		while(true){
			
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
