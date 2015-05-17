package backup;



public class Vertex implements Comparable<Vertex>
{
	
public static int counter = 0;
private int ID = 0;
private String info="no info";
private double weight=1;


public Vertex()
{
	Vertex.counter++;
	this.ID = counter;
	
}

public Vertex(String info)
{
	Vertex.counter++;
	this.ID = counter;
	this.info=info;
	
}	

public Vertex(double weight) {
	Vertex.counter++;
	this.ID = counter;
	this.weight = weight;
	
}

public Vertex( String info, double weight) {
	Vertex.counter++;
	this.ID = counter;
	this.info = info;
	this.weight = weight;
	
}


public String getInfo() {
	return info;
}
public void setInfo(String info) {
	this.info = info;
}
public double getWeight() {
	return weight;
}
public void setWeight(double weight) {
	this.weight = weight;
}

public int getID() {
	return ID;
}

public void setID(int iD) {
	ID = iD;
}

@Override
public int compareTo(Vertex o) {
	
	if (this.ID > o.ID)
	{
		return 1;
	}
	

	if(this.ID < o.ID)
	{
		return -1;
	}
	else
		return 0;

}




}