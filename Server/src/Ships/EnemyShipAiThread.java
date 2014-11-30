package Ships;

import sd3.GameState;
import sd3.Ship;
import sd3.Vector2;

public class EnemyShipAiThread extends Thread {

	private Vector2 _pm;
	private Ship _player;
	private Ship _enemy;
	private GameState _gs;
	
	public EnemyShipAiThread(Ship enemy,Vector2 pm,Ship player,GameState gs) {
		this._pm = pm;
		this._player = player;
		this._gs = gs;
		this._enemy = enemy;
	}

	@Override
	public void run() {
		//System.out.println("Starting thread");
		_enemy.Heal();
		Vector2 m = _enemy.getMove();
		
		//Check for combat
		if(m.x == _pm.x && m.y == _pm.y){
			_enemy.Attack(_player);
		}
		
		//check for dead ships
		if (_enemy.isDead()){
			_gs.DeleteShip(_enemy);
		}else{
			_gs.AddMove(_enemy, m.x, m.y);
		}
	}

}
