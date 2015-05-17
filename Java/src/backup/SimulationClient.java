package backup;


import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

import master.Avatar;
import master.Peer;

public class SimulationClient implements Serializable, Runnable
{
	
	private static final long serialVersionUID = 1L;
	private static int counter = 0;
	private static int basePortNumber = 50055;
	private Peer peer;
	private String coordinatorAddress;
	private String serverSocketInfo;
	private int serverPort = 0;
	
	public SimulationClient(String serverAddress, int serverPort)
	{
		try
		{
			SimulationClient.counter++;
			this.peer = new Peer(new Avatar());
			this.coordinatorAddress = serverAddress;
			this.serverPort = serverPort;
			this.serverSocketInfo = String.valueOf(basePortNumber + SimulationClient.counter);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		
	}
	
	@Override
	public void run() 
	{
		connectToCoordinator(this.coordinatorAddress, this.serverPort);
		waitForCoordinatorUpdates();
	}
	
	private void waitForCoordinatorUpdates() 
	{
		
		try
		{
			ServerSocket receiveSocket = new ServerSocket(Integer.parseInt(serverSocketInfo));
			
			while(true)
			{
				Socket clientSocket = receiveSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
				
				Object update = in.readObject();
				if(update instanceof Peer)
				{
					this.setPeer((Peer)update);
					out.writeObject("Update successfull!");
					out.flush();
				}
				
				if(update instanceof String)
				{
					String message = (String)update;
					if(message.equals("Simulation done"))
					{
						receiveSocket.close();
						break;
					}
				}
				
				out.close();
				in.close();
				clientSocket.close();
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void shutDown()
	{
		
	}

	public void connectToCoordinator(String serverAddress, int serverPort)
	{
		try
		{
			Socket serverConnection = new Socket(serverAddress, serverPort);
			ObjectOutputStream out = new ObjectOutputStream(serverConnection.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(serverConnection.getInputStream());
		
			
			out.writeObject(this);
			out.flush();
			Object receivedObject = in.readObject();
			if(receivedObject instanceof String)
			{
				System.out.println((String)receivedObject);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	

	public String getServerAddress() {
		return coordinatorAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.coordinatorAddress = serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	public String getServerSocketInfo() {
		return serverSocketInfo;
	}

	public void setServerSocketInfo(String serverSocketInfo) {
		this.serverSocketInfo = serverSocketInfo;
	}



	

}
