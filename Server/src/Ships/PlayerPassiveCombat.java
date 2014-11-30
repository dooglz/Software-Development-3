package Ships;


public class PlayerPassiveCombat extends StandardShipCombat {

	public PlayerPassiveCombat() {
		super();
		_strength = 1;
		_maxHealth = 2;
		_health = _maxHealth;
	}

}
