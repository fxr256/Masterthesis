package master;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class MMVEObject implements Comparable<MMVEObject>, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int counter = 0;
	private int ID = 0;
	private String info="no info";
	private Point position;
	private int weight = 1;
	private ArrayList<MMVEObject> interestedIn;
	private int mobiSimID = 0;
	
	public MMVEObject()
	{
		MMVEObject.counter++;
		this.ID = counter;
		this.position = new Point();
		this.interestedIn = new ArrayList<MMVEObject>();
		
	}

	public MMVEObject(String info)
	{
		MMVEObject.counter++;
		this.ID = counter;
		this.info=info;
		this.position = new Point();
		this.interestedIn = new ArrayList<MMVEObject>();
		
	}
	
	public MMVEObject(Point pos)
	{
		MMVEObject.counter++;
		this.ID = counter;
		this.position = pos;
		this.interestedIn = new ArrayList<MMVEObject>();
		
	}
	

	public MMVEObject( String info, Point pos) {
		MMVEObject.counter++;
		this.ID = counter;
		this.info = info;
		this.position = pos;
		this.interestedIn = new ArrayList<MMVEObject>();
		
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
	@Override
	public int compareTo(MMVEObject o) {
		
		if (this.ID > o.getID())
		{
			return 1;
		}
		

		if(this.ID < o.getID())
		{
			return -1;
		}
		else
			return 0;

	}

	public ArrayList<MMVEObject> getInterestedIn() {
		return interestedIn;
	}

	public void setInterestedIn(ArrayList<MMVEObject> interestedIn) {
		this.interestedIn = interestedIn;
	}
	
	public boolean addInterest(MMVEObject interestingObject)
	{
		if(interestingObject == null)
			return false;
		
		if(interestingObject.equals(this))
			return false;
		
		//check if we are already interested in this object
		if(this.interestedIn.contains(interestingObject))
			return false;
		else
		{
			//add it to interestedIn, returns the return value of add so further errors can be detected
			return this.interestedIn.add(interestingObject);
		}
	}
	
	public boolean removeInterest(MMVEObject objectToBeRemoved)
	{
		if(objectToBeRemoved == null)
			return false;
		
		if(objectToBeRemoved.equals(this))
			return false;
		
		//return false if the object can't be found
		if(!this.interestedIn.contains(objectToBeRemoved))
			return false;
		else
		{
			return this.interestedIn.remove(objectToBeRemoved);
		}
	}

	public int getMobiSimID() {
		return mobiSimID;
	}

	public void setMobiSimID(int mobiSimID) {
		this.mobiSimID = mobiSimID;
	}

}
