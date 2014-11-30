package sd3;
import java.util.ArrayList;

import sd3.GameState.Move;
import Ships.*;

public class Game {
	private ArrayList<Ship> _enemies;
	private PlayerShip _player;
	public boolean running;
	public ArrayList<GameState> _states;
	
	public Game()
	{	
		_states = new ArrayList<GameState>();
		_enemies = new ArrayList<Ship>();
		_player = new PlayerShip((byte)1, (byte)1);
		//_enemies.add(new BattleStar());
		running = true;
	}
	
	public void Update()
	{	
		GameState newState = new GameState();
		
		_player.Heal();
		Vector2 pm = _player.getMove();
		newState.AddMove(_player, pm.x, pm.y);
		
		//update  all enemies
		for(int i =0; i<_enemies.size();i++){
			Ship s = _enemies.get(i);
			s.Heal();
			
			Vector2 m = s.getMove();
			
			//Check for combat
			s.Heal();
			if(m.x == pm.x && m.y == pm.y){
				s.Attack(_player);
			}
			
			//check for dead ships
			if (s.isDead()){
				newState.DeleteShip(s);
			}else{
				newState.AddMove(s, m.x, m.y);
			}
		}
		
		if(_player.isDead()){
			//Game over
			System.out.println("Game over");
			running = false;
		}

		//spawn new enemy
		if (Math.random() > (1d/3d)){
			newState.CreateShip("BattleStar");
		}
		_states.add(newState);
		ExecuteState(newState);
		
	}
	
	public void ExecuteState(GameState state){		
		ArrayList<Ship> updated = new ArrayList<Ship>(_enemies);
		//move existing ships to new position
		for(Move m : state._moves)
		{
			m.s.WarpTo(m.pos.x, m.pos.y);
			updated.remove(m.s);
		}
		//remove existing ships that have died
		for(Ship s : state._removedships)
		{
			_enemies.remove(s);
			updated.remove(s);
		}
		//add new ships
		for(String s : state._newships)
		{
			if(_enemies.contains(s)){
				//TODO
			}else{
				_enemies.add(new BattleStar());
			}
		}
		//
		for(Ship s : updated)
		{
			//we have a ship that doesn't exist in this state, 
			//it must have come form the future. Remove it
			_enemies.remove(s);
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
		_player.setAggressive(mode);
		Update();
		return;
	}
	
	public void undo()
	{
		//can we undo?
		if(_states.size() >1)
		{
			System.out.println("Undoing");
			//yes, pop off recent move
			_states.remove(_states.size()-1);
			//execute back
			ExecuteState(_states.get(_states.size()-1));
		}
	}
	
}
