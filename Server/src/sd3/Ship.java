package sd3;

public class Ship {
	protected byte _x;
	protected byte _y;
	protected byte _maxHealth;
	protected byte _health;
	protected byte _strength;
	protected String _type;
	
	public Ship(byte x, byte y){
		_x = x;
		_y = y;
	}
	
	public void Attack(Ship s){
		s.TakeDamage(_strength);
		this.TakeDamage(s.GetStrength());
	}
	
	public void TakeDamage(byte dmg){
		_health -= dmg;
	}
	
	public void Heal(){
		_health = _maxHealth;
	}
	
	public byte GetHealth(){
		return _health;
	}
	
	public byte GetStrength(){
		return _strength;
	}
	
	public boolean ValdateMove(byte x, byte y){
		if (x < 0 || y < 0 || x > 3 || y > 3){
			return false;
		}
		if (x > (_x+1) || x < (_x-1)){
			return false;
		}
		if (y > (_y+1) || y < (_y-1)){
			return false;
		}
		return true;
	}
	
	public void Move(byte x, byte y){
		_x = x;
		_y = y;
	}
	
	public byte GetX(){
		return _x;
	}
	
	public byte GetY(){
		return _y;
	}
	
	public String GetType(){
		return _type;
	}
}
