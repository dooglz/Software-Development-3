package sd3;

import java.util.ArrayList;

public class GameState {
	protected class Move{
		public Ship s;
		Vector2 pos;
		public Move(Ship s,byte x, byte y){
			this.s  = s; 
			this.pos = new Vector2(x,y);
		}
	}
	protected class NewShip{
		Vector2 pos;
		String type;
		public NewShip(String type, byte x, byte y){
			this.type =type;
			this.pos = new Vector2(x,y);
		}
	}
	
	ArrayList<Move> _moves;
	ArrayList<NewShip> _newships;
	ArrayList<Ship> _removedships;
	
	public GameState() {
		
	}

	public void AddMove(Ship s,byte x, byte y)
	{
		_moves.add(new Move(s,x,y));
	}
	
	public void CreateShip(String type, byte x, byte y)
	{
		_newships.add(new NewShip(type,x,y));
	}
	
	public void DeleteShip(Ship s)
	{
		_removedships.add(s);
	}
	
	public void Execute()
	{
		
	}
}
