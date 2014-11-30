package Ships;

import sd3.Ship;

public interface CombatMode {

	void Attack(Ship s);
	void TakeDamage(byte dmg);
	boolean isDead();
	void Heal();
	byte GetStrength();
	
}
