package sd3;
import java.util.ArrayList;

import Ships.*;

public class Game {
	private ArrayList<Ship> _enemies;
	private PlayerShip _player;
	public boolean running;
	
	public Game()
	{	
		_enemies = new ArrayList<Ship>();
		_player = new PlayerShip((byte)1, (byte)1);
		//_enemies.add(new BattleStar());
		running = true;
	}
	
	public void Update()
	{		
		_player.Heal();
		{
			byte x; 
			byte y;
			do {
				x = (byte)((int)(Math.random() * (5))-1);
				y = (byte)((int)(Math.random() * (5))-1);
			} while(!_player.ValdateMove(x, y));
			_player.Move(x, y);
		}
		
		//update  all enemies
		for(int i =0; i<_enemies.size();i++){
			Ship s = _enemies.get(i);
		
			s.Heal();
			byte x; 
			byte y;
			do {
				x = (byte)((int)(Math.random() * (5))-1);
				y = (byte)((int)(Math.random() * (5))-1);
			} while(!s.ValdateMove(x, y));
			s.Move(x, y);
			
			//Check for combat
			s.Heal();
			if(x == _player.GetX() && y == _player.GetY()){
				s.Attack(_player);
			}
			
			//check for dead ships
			if (s.GetHealth() <= 0){
				_enemies.remove(s);
				i--;
				System.out.println("Dead ship");
			}
		}
		
		if(_player.GetHealth() <= 0){
			//Game over
			System.out.println("Game over");
			running = false;
		}
		
		
		//spawn new enemy
		if (Math.random() > (1d/3d)){
			_enemies.add(new BattleStar());
		}
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
	
	public void turn(boolean mode){
		Update();
		return;
	}
	
}
