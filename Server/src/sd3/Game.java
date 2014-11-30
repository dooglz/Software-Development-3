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
		_player.Move();
		
		//update  all enemies
		for(int i =0; i<_enemies.size();i++){
			Ship s = _enemies.get(i);
			s.Heal();
			s.Move();
			//Check for combat
			s.Heal();
			if(s.GetX() == _player.GetX() && s.GetY() == _player.GetY()){
				s.Attack(_player);
			}
			
			//check for dead ships
			if (s.isDead()){
				_enemies.remove(s);
				i--;
			}
		}
		
		if(_player.isDead()){
			//Game over
			System.out.println("Game over");
			running = false;
		}
	
		//sAggpawn new enemy
		if (Math.random() > (1d/3d)){
			//_enemies.add(new BattleStar());
		}
		_enemies.add(new BattleStar());
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
		_player.setAggressive(mode);
		Update();
		return;
	}
	
}
