package sd3;

import sd3.Vector2;
import Ships.CombatMode;
import Ships.MoveMethod;
import Ships.Random9WayMovement;
import Ships.StandardShipCombat;

public abstract class Ship {
	protected byte _x;
	protected byte _y;
	protected String _type;
	protected MoveMethod _moveMethod;
	protected CombatMode _combatMode;
	
	public Ship(byte x, byte y){
		_x = x;
		_y = y;
		_moveMethod = new Random9WayMovement();
		_combatMode = new StandardShipCombat();
	}
	
	public MoveMethod getMoveMethod() {
		return _moveMethod;
	}

	public void setMoveMethod(MoveMethod _moveMethod) {
		this._moveMethod = _moveMethod;
	}

	public CombatMode getCombatMode() {
		return _combatMode;
	}

	public void setCombatMode(CombatMode _combatMode) {
		this._combatMode = _combatMode;
	}

	public void Attack(Ship s){
		_combatMode.Attack(s);
	}
	
	public void Heal(){
		_combatMode.Heal();
	}
	
	public boolean isDead(){
		return _combatMode.isDead();
	}
	
	public void WarpTo(byte x, byte y){
		_x = x;
		_y = y;
	}
	
	public void Move(){
		Vector2 v =  _moveMethod.nextMove(_x,_y);
		_x = v.x;
		_y = v.y;
	}
	
	public Vector2 getMove(){
		return _moveMethod.nextMove(_x,_y);
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
