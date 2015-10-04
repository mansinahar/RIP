import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Client implements Runnable {

	DatagramSocket socket;
	static RoutingTable myRoutingTable = new RoutingTable();
	static ArrayList<Neighbor> neighbors = new ArrayList<Neighbor>();

	public Client() {
		try {
			socket = new DatagramSocket(6455);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method return the routing table of this client
	 */
	public RoutingTable getRoutingTable() {
		return myRoutingTable;
	}

	/*
	 * This method sends the routing table to all it's neighbors
	 */
	public void sendRoutingTable() {
		
		System.out.println("Sending routing table to neighbors.");
		Iterator<Neighbor> neighborIterator = neighbors.iterator();

		try {
			
			// Pack object into datagram packet
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			ObjectOutputStream os = new ObjectOutputStream(outputStream);
			os.writeObject(myRoutingTable);
			byte[] buffer = outputStream.toByteArray();
			DatagramPacket packet;


			while(neighborIterator.hasNext()) {

				//send myRoutingTable to neighbor
				Neighbor nextNeighbor = neighborIterator.next();
				packet = new DatagramPacket(buffer, buffer.length, nextNeighbor.neighborAddress, nextNeighbor.neighborPort);
				socket.send(packet);

			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}


	/*
	 * This method adds a new neighbor to the neighbor list
	 * @param neighborAddress
	 * 			InetAddress of the neighbor to be added
	 * @param port
	 * 			Port at which the neighbor is connected
	 */
	public void addNeighborInfo(InetAddress neighborAddress, int port) {
		Neighbor newNeighbor = new Neighbor();
		newNeighbor.neighborAddress = neighborAddress;
		newNeighbor.neighborPort = port;

		neighbors.add(newNeighbor);
	}
	
	/*
	 * This method adds a new neighbor to the neighbor list
	 * @param neighbor
	 * 			Neighbor object that needs to be added to the neighbor list
	 */
	public void addNeighborInfo(Neighbor neighbor) {
		neighbors.add(neighbor);
	}
	

	/*
	 * This method checks if it's own routing table needs to be updated
	 */
	public void checkForUpdates(RoutingTable routingTable) {
		//Check if the routing table needs to be updated
		synchronized(myRoutingTable){

			//If yes, update it and send updates to all neighbors
			sendRoutingTable();
			//If no, do nothing.
		}
	}

	@Override
	public void run() {

		while(true) {
			sendRoutingTable();
			try {
				Thread.sleep(3000);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}

	}
	
	
	/*
	 * This method adds the destinations in the system to the routing table
	 * If the destination is a neighbor then adds it to the neighbor's list.
	 */
	public void addDestinations(String destAddress, boolean isNeighbor, int port) throws Exception {
		
		// If it is a neighbor then nexthop would be the neighbor itself and cost would be 1
		if(isNeighbor) {
			Neighbor newNeighbor = new Neighbor();
			newNeighbor.neighborAddress = InetAddress.getByName(destAddress);
			newNeighbor.neighborPort = port;
			addNeighborInfo(newNeighbor);
			myRoutingTable.addEntry(newNeighbor.neighborAddress, newNeighbor.neighborAddress.getHostAddress(), 1);
		}
		// If not neighbor then nexthop is not yet knows and cost would be infinity
		else {
			myRoutingTable.addEntry(InetAddress.getByName(destAddress), "-\t", Integer.MAX_VALUE);
		}
	}
	
	
	public static void main(String args[]) throws Exception {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Enter the number of destinations in system: ");
		int n = Integer.parseInt(sc.next());
		
		
		Client thisClient = new Client();
		
		// Taking information for all the destinations in the systems
		// and initializing this router's routing table.
		for(int i = 0; i < n; ++i) {
			String destAddress;
			boolean isNeighbor;
			int port = 0;
			
			System.out.println("Enter destination IP address: ");
			destAddress = sc.next();
			System.out.println("Is this destination a neighbor?");
			isNeighbor = ((sc.next().compareToIgnoreCase("yes")) == 0);
			
			if(isNeighbor) {
				System.out.println("Enter the port number for the neighbor: ");
				port = Integer.parseInt(sc.next());
			}
			
			thisClient.addDestinations(destAddress, isNeighbor, port);
		}
		myRoutingTable.printTable();
		thisClient.sendRoutingTable();
		
		// Closing the scanner object
		sc.close();
	}

}
