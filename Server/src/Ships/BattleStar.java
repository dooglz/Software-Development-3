package Ships;
import sd3.Ship;

public class BattleStar extends Ship {

	public BattleStar() {
		super((byte)0, (byte)0);
		_type = "BattleStar";
		_strength = 1;
		_maxHealth = 1;
		_health = _maxHealth;
	}

}
