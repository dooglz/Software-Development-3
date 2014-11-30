package sd3;

import java.util.ArrayList;

public class GameState {
	public class Move{
		public Ship s;
		Vector2 pos;
		public Move(Ship s,byte x, byte y){
			this.s  = s; 
			this.pos = new Vector2(x,y);
		}
	}
	
	ArrayList<Move> _moves;
	ArrayList<String> _newships;
	ArrayList<Ship> _removedships;
	
	public GameState() {
		_moves = new ArrayList<Move>();
		_newships = new ArrayList<String>();
		_removedships = new ArrayList<Ship>();
	}

	public void AddMove(Ship s,byte x, byte y)
	{
		_moves.add(new Move(s,x,y));
	}
	
	public void CreateShip(String type)
	{
		_newships.add(type);
	}
	
	public void DeleteShip(Ship s)
	{
		_removedships.add(s);
	}

}
