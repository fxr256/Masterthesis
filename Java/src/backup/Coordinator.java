package backup;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;

import master.Peer;


public class Coordinator implements Runnable
{
	private final int serverPort = 50055;
	private CoordinatorTask serverTask;
	private ArrayList<SimulationClient> registeredClients;
	private int executionDelay = 0;
	private boolean running = true;
	private int peerNumber = 0;
	private ServerSocket serverSocket;
	
	public Coordinator(int periodNumber, String avatarFile, String objectFile, String interestedInFile, 
			String logFile, String interactionFile, int peerNumber, int executionInterval, String configFile)
	{
		this.registeredClients = new ArrayList<SimulationClient>();
		this.peerNumber = peerNumber;
		this.serverTask = new CoordinatorTask(periodNumber, avatarFile, objectFile, interestedInFile, logFile, interactionFile, this);
		this.executionDelay = executionInterval;
				
	}
	
	public void run()
	{
		try
		{
			serverSocket = new ServerSocket(serverPort);
			
			while(running)
			{
				Socket clientSocket = serverSocket.accept();
				ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
				Object readobject = null;
				if((readobject = in.readObject()) instanceof SimulationClient)
				{
					SimulationClient clientToAdd = (SimulationClient) readobject;
					registeredClients.add(clientToAdd);
					//add peer to server task
					serverTask.getPeers().add(clientToAdd.getPeer());
					out.writeObject("Registration successfull!");
				}
				else
				{
					out.writeObject("Unknown command");
				}
				
				boolean scheduled = false;
				
				if(this.registeredClients.size() == peerNumber && !scheduled)
				{
					Timer simulationTimer = new Timer();
					simulationTimer.schedule(this.serverTask,0, this.executionDelay);
					scheduled = true;
				}
			
			}
			
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	public void propagateUpdates()
	{
		Iterator<SimulationClient> itClients = registeredClients.iterator();
		while(itClients.hasNext())
		{
			SimulationClient clientToUpdate = itClients.next();
			Peer peerToSend = getMatchingPeerFromCoordinatorTask(clientToUpdate.getPeer());
			if(peerToSend != null)
			{
				try
				{
					Socket connectionToClient = new Socket("127.0.0.1", Integer.parseInt(clientToUpdate.getServerSocketInfo()));
					ObjectOutputStream out = new ObjectOutputStream(connectionToClient.getOutputStream());
					ObjectInputStream in = new ObjectInputStream(connectionToClient.getInputStream());
					
					out.writeObject(peerToSend);
					out.flush();
					
					Object response = in.readObject();
					if(response instanceof String)
					{
						System.out.println((String)response);
					}
					
					out.close();
					in.close();
					connectionToClient.close();
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
		}
		
	}
	
	public void shutDownClients()
	{
		
		Iterator<SimulationClient> itClients = registeredClients.iterator();
		while(itClients.hasNext())
		{
			SimulationClient clientToUpdate = itClients.next();
			
			try
			{
				Socket connectionToClient = new Socket("127.0.0.1", Integer.parseInt(clientToUpdate.getServerSocketInfo()));
				ObjectOutputStream out = new ObjectOutputStream(connectionToClient.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(connectionToClient.getInputStream());
				
				out.writeObject("Simulation done");
				out.flush();
				
				out.close();
				in.close();
				connectionToClient.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
				
			
		}
		
	}
	
	private Peer getMatchingPeerFromCoordinatorTask(Peer peer) 
	{
		Iterator<Peer> itPeers = serverTask.getPeers().iterator();
		while(itPeers.hasNext())
		{
			Peer peerToCheck = itPeers.next();
			if(peerToCheck.equals(peer))
			{
				return peer;
			}
		}
		
		return null;
	}

	public void shutDown()
	{
		try
		{
			this.running = false;
			this.serverSocket.close();
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public CoordinatorTask getServerTask() {
		return serverTask;
	}

	public void setServerTask(CoordinatorTask serverTask) {
		this.serverTask = serverTask;
	}

	public ArrayList<SimulationClient> getRegisteredClients() {
		return registeredClients;
	}

	public void setRegisteredClients(ArrayList<SimulationClient> registeredClients) {
		this.registeredClients = registeredClients;
	}

	public int getExecutionDelay() {
		return executionDelay;
	}

	public void setExecutionDelay(int executionDelay) {
		this.executionDelay = executionDelay;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public int getServerPort() {
		return serverPort;
	}
	
	
	

}
