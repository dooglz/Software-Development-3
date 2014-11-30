package Ships;

public interface MoveMethod {
	public class Vector2
	{
		public Vector2(byte x,byte y){this.x = x; this.y = y;}
	    public byte x;
	    public byte y;
	}
	
	boolean ValdateMove(byte currentX, byte currentY,byte x, byte y);
	Vector2 nextMove(byte currentX, byte currentY);
}
