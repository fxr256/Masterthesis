package master;

import java.awt.Point;
import java.io.Serializable;

//represents a circular Area of Interest, based on a class representing a circle, which I found here:
// http://introcs.cs.princeton.edu/35purple/Circle.java.html
public class AoI implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Point center;
    private double radius;
   
    public AoI(Point center, double radius) 
    {
        this.center = center;
        this.radius = radius;
    }

    public boolean contains(Point p) 
    {  
    	return p.distance(center) <= radius; 
    }

    public double area()      
    { 
    	return Math.PI * radius * radius; 
    }
    
    public double perimeter() 
    { 
    	return 2 * Math.PI * radius;      
    }

    //checks whether two AoIs intersect by comparing the distance between the centers of the circles
    //with the sum of their radii, if the distance is bigger, then they don't intersect
    //this calculates the euclidan distance: sqrt[(x2-x1)²+(y2-y1)²]
    public boolean intersects(AoI c) 
    {
    	double distance = center.distance(c.center);
    	//double radii = radius + c.radius;
    	//originally radii was checked in the return statement, however this means that each edge is bidirectional
    	//this should be fixed by only checking the radius of the current AoI
        return  distance <= this.radius; 
    }
    
    //overloaded version of intersects for points, so it an be used for mmveobjects
    public boolean intersectes(Point p)
    {
    	double distance = center.distance(p);
    	return  distance <= this.radius; 
    }

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}


}
