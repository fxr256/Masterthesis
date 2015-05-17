package master;

import java.io.Serializable;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

public class Peer implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int counter = 0;
	private int ID = 0;
	private String info = "no info";
	private int capacity = 100;
	private int peerCopyID = 0;
	private DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> managedObjects; 
	private boolean overloaded = false;
	private Avatar ownAvatar;
	
	
	public Peer()
	{
		Peer.counter++;
		this.ID = Peer.counter;
		this.managedObjects = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	public Peer(String description)
	{
		Peer.counter++;
		this.ID = Peer.counter;
		this.info = description;
		this.managedObjects = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	public Peer(int capacity)
	{
		Peer.counter++;
		this.ID = Peer.counter;
		this.capacity = capacity;
		this.managedObjects = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	public Peer(Avatar avatar)
	{
		Peer.counter++;
		this.ID = Peer.counter;
		this.ownAvatar = avatar;
		this.managedObjects = new DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>(DefaultWeightedEdge.class);
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

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge>getManagedObjects() {
		return managedObjects;
	}

	public void setManagedObjects(DirectedWeightedMultigraph<MMVEObject,DefaultWeightedEdge> managedObjects) {
		this.managedObjects = managedObjects;
	}

	public boolean isOverloaded() {
		return overloaded;
	}

	public void setOverloaded(boolean overloaded) {
		this.overloaded = overloaded;
	}

	public Avatar getOwnAvatar() {
		return ownAvatar;
	}

	public void setOwnAvatar(Avatar ownAvatar) {
		this.ownAvatar = ownAvatar;
	}

	public int getPeerCopyID() {
		return peerCopyID;
	}

	public void setPeerCopyID(int peerCopyID) {
		this.peerCopyID = peerCopyID;
	}

}
