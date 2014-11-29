package Ships;

import sd3.Ship;

public class PlayerShip extends Ship {

	public PlayerShip(byte x, byte y) {
		super(x, y);
		_type = "player";
		_strength = 1 ;
		_maxHealth = 2;
		_health = _maxHealth;
	}

}
