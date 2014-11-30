package Ships;

import sd3.Vector2;


public interface MoveMethod {
	
	boolean ValdateMove(byte currentX, byte currentY,byte x, byte y);
	Vector2 nextMove(byte currentX, byte currentY);
}
