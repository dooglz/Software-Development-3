package Ships;

import sd3.Ship;

public class PlayerShip extends Ship {

	public PlayerShip(byte x, byte y) {
		super(x, y);
		_type = "player";
		_combatMode = new PlayerPassiveCombat();
	}

	public void setAggressive(boolean b)
	{
		if(b){
			_combatMode = new PlayerPassiveCombat();
		}else{
			_combatMode = new PlayerAggressiveCombat();
		}
	}
}
