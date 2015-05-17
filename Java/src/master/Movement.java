package master;

import java.awt.Point;

public class Movement 
{
	
	private int round = 0;
	private int movingObjectID = 0;
	private Point newPosition = null;
	
	public Movement(int round, int movingObjectID, Point newPostion)
	{
		this.round = round;
		this.movingObjectID = movingObjectID;
		this.newPosition = newPostion;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getMovingObjectID() {
		return movingObjectID;
	}

	public void setMovingObjectID(int movingObjectID) {
		this.movingObjectID = movingObjectID;
	}

	public Point getNewPosition() {
		return newPosition;
	}

	public void setNewPosition(Point newPosition) {
		this.newPosition = newPosition;
	}
	
	

}
