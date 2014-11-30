package Ships;

public class PlayerAggressiveCombat extends StandardShipCombat {

	public PlayerAggressiveCombat() {
		super();
		_strength = 1;
		_maxHealth = 3;
		_health = _maxHealth;
	}

}
