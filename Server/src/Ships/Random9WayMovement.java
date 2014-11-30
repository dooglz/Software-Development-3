package Ships;

public class Random9WayMovement implements MoveMethod {

	@Override
	public boolean ValdateMove(byte currentX, byte currentY, byte x, byte y) {
		if (x < 0 || y < 0 || x > 3 || y > 3){
			return false;
		}
		if (x > (currentX+1) || x < (currentX-1)){
			return false;
		}
		if (y > (currentY+1) || y < (currentY-1)){
			return false;
		}
		return true;

	}

	@Override
	public Vector2 nextMove(byte currentX, byte currentY) {
		byte x; 
		byte y;
		do {
			x = (byte)((int)(Math.random() * (5))-1);
			y = (byte)((int)(Math.random() * (5))-1);
		} while(!ValdateMove(currentX,currentY,x, y));
		
		return new Vector2(x, y);
	}

}
