package backup;

public class GraphEmptyException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GraphEmptyException()
	{
		super();
	}
	
	public GraphEmptyException(String description)
	{
		super(description);
	}

}
