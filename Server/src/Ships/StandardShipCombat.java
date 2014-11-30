package Ships;

import sd3.Ship;

public class StandardShipCombat implements CombatMode {

	protected byte _maxHealth;
	protected byte _health;
	protected byte _strength;
	
	public StandardShipCombat()
	{
		_strength = 1;
		_maxHealth = 1;
		_health = _maxHealth;
	}
	
	@Override
	public void Attack(Ship s) {
		s.getCombatMode().TakeDamage(_strength);
		this.TakeDamage(s.getCombatMode().GetStrength());
	}

	@Override
	public void TakeDamage(byte dmg) {
		_health -= dmg;
	}

	@Override
	public boolean isDead() {
		return (_health <= 0);
	}

	@Override
	public void Heal() {
		_health = _maxHealth;
	}

	@Override
	public byte GetStrength() {
		return _strength;
	}

}
