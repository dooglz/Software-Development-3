import java.util.ArrayList;

public class Game {
	private ArrayList<Ship> _enemies;
	private Ship _player;
	public boolean running;
	
	public Game()
	{	
		_player = new Ship((byte)(Math.random() * (5)),(byte)(Math.random() * (5)));
		running = true;
	}
	
	public void Update()
	{
		//spawn new enemy
		if (Math.random() > (1d/3d)){
			_enemies.add(new Ship((byte)0,(byte)0));
		}
		
		//update  all enemies
		for(Ship s : _enemies){
			byte x; 
			byte y;
			do {
				x = (byte)((int)(Math.random() * (3))-1);
				y = (byte)((int)(Math.random() * (3))-1);
			} while(!s.ValdateMove(x, y));
			s.Move(x, y);
			
			//Check for combat
			s.Heal();
			if(x == _player.GetX() && y == _player.GetY()){
				s.Attack(_player);
			}
			
			//check for dead ships
			if (s.GetHealth() < 0){
				_enemies.remove(s);
			}
		}
		
		if(_player.GetHealth() < 0){
			//Game over
			running = false;
		}
		_player.Heal();
	}
	
	public ArrayList<Ship> GetEnemies() {
		return _enemies;
	}

	public void setEnemies(ArrayList<Ship> enemies) {
		_enemies = enemies;
	}

	public Ship GetPlayer() {
		return _player;
	}

	public void SetPlayer(Ship player) {
		_player = player;
	}
	
}
