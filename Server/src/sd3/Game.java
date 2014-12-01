package sd3;
import java.util.ArrayList;

import sd3.GameState.Move;
import Ships.*;

public class Game {
	private ArrayList<Ship> _enemies;
	private PlayerShip _player;
	public boolean running;
	public ArrayList<GameState> _states;
	public String[] Shiptypes = {"BattleStar","BattleCruiser","BattleShooter"};
	
	public Game()
	{	
		_states = new ArrayList<GameState>();
		_enemies = new ArrayList<Ship>();
		_player = new PlayerShip((byte)1, (byte)1);
		running = true;
	}
	
	public void Update()
	{	
		GameState newState = new GameState();
		
		_player.Heal();
		Vector2 pm = _player.getMove();
		newState.AddMove(_player, pm.x, pm.y);
		
		//Update all enemies
		ArrayList<Thread> threads = new ArrayList<Thread>();
		for(int i =0; i<_enemies.size();i++){
			Thread t = new EnemyShipAiThread(_enemies.get(i),pm, _player, newState);
			threads.add(t);
			t.start();
		}
		
		//wait for completion
		for (Thread thread : threads) {
			 try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
			
		//System.out.println("Threads done");
		
		//Is Game over?
		if(_player.isDead()){
			System.out.println("Game over");
			running = false;
		}

		//Spawn new enemy
		if (Math.random() > (1d/3d)){
			int i = (int) Math.floor(Math.random() * Shiptypes.length);
			newState.CreateShip(Shiptypes[i]);
		}
		
		//put state on stack and update
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
				if(s.equals("BattleStar")){
					_enemies.add(new BattleStar());
				}else if(s.equals("BattleCruiser")){
					_enemies.add(new BattleCruiser());
				}else{
					_enemies.add(new BattleShooter());
				}
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
