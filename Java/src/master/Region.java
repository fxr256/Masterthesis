package master;

import java.awt.Rectangle;
import java.util.ArrayList;

public class Region 
{
	public static int counter = 0;
	private int ID = 0;
	private Rectangle graphicalRepresentation;
	private ArrayList<MMVEObject> objectsInRegion;
	private Peer managingPeer;
	private boolean remainingRegion = false;
	
	public Region(int x, int y, int width, int height)
	{
		Region.counter++;
		graphicalRepresentation = new Rectangle(x, y, width, height);
		objectsInRegion = new ArrayList<MMVEObject>();
		this.ID = counter;
	}
	
	public Region(int x, int y, int width, int height, Peer managingPeer)
	{
		Region.counter++;
		graphicalRepresentation = new Rectangle(x, y, width, height);
		objectsInRegion = new ArrayList<MMVEObject>();
		this.managingPeer = managingPeer;
		this.ID = counter;
	}
	
	public Rectangle getGraphicalRepresentation() {
		return graphicalRepresentation;
	}

	public void setGraphicalRepresentation(Rectangle graphicalRepresentation) {
		this.graphicalRepresentation = graphicalRepresentation;
	}

	public ArrayList<MMVEObject> getObjectsInRegion() {
		return objectsInRegion;
	}

	public void setObjectsInRegion(ArrayList<MMVEObject> objectsInRegion) {
		this.objectsInRegion = objectsInRegion;
	}

	public Peer getManagingPeer() {
		return managingPeer;
	}

	public void setManagingPeer(Peer managingPeer) {
		this.managingPeer = managingPeer;
	}

	public boolean isRemainingRegion() {
		return remainingRegion;
	}

	public void setRemainingRegion(boolean remainingRegion) {
		this.remainingRegion = remainingRegion;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

}
