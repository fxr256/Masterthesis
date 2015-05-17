package master;

import java.awt.Point;

public class Avatar extends MMVEObject implements Comparable<MMVEObject>
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private int ID = 0;
	//private String info="no info";
	//private Point position;
	private AoI aoi;
	
	//default position = 0.0 and AoI-Size = 1
	public Avatar()
	{
		//Avatar.counter++;
		//this.ID = counter;
		//this.position = new Point();
		super();
		this.aoi = new AoI (this.getPosition(), 1);
	}

	public Avatar(String info)
	{
		super(info);
		this.aoi = new AoI (this.getPosition(), 1);
	}
	
	public Avatar(Point pos)
	{
		super(pos);
		this.aoi = new AoI (this.getPosition(), 1);
	}
	
	public Avatar(Point pos, int AoIsize)
	{
		super(pos);
		this.aoi = new AoI (this.getPosition(), AoIsize);
	}
	

	public Avatar( String info, Point pos) {
		super(info,pos);
		this.aoi = new AoI (this.getPosition(), 1);
	}
	
	@Override
	public int compareTo(MMVEObject o) {
		
		if (this.getID() > o.getID())
		{
			return 1;
		}
		

		if(this.getID() < o.getID())
		{
			return -1;
		}
		else
			return 0;

	}

	public AoI getAoi() {
		return aoi;
	}

	public void setAoi(AoI aoi) {
		this.aoi = aoi;
	}
	
	

}
