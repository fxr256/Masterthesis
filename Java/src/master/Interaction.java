package master;

public class Interaction 
{
	
	private MMVEObject source;
	private MMVEObject target;
	private int round = 0;
	
	public Interaction(MMVEObject source, MMVEObject target)
	{
		this.source = source;
		this.target = target;
		
	}
	
	public Interaction(MMVEObject source, MMVEObject target, int round)
	{
		this.source = source;
		this.target = target;
		
		this.round = round;
	}

	public MMVEObject getSource() {
		return source;
	}

	public void setSource(MMVEObject source) {
		this.source = source;
	}

	public MMVEObject getTarget() {
		return target;
	}

	public void setTarget(MMVEObject target) {
		this.target = target;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

}
