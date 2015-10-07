/*
 *@author Mansi Nahar 
 */
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.*;
public class Server implements Runnable {

	DatagramSocket socket;
	static Logging log;
	
	@Override
	public void run() {
		
		try {
		log = new Logging("Server" + InetAddress.getLocalHost().getHostName());
		log.write("Server started");

		//Creating datagram socket
		
			socket = new DatagramSocket(6499);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		// Keep receiving packets
		while(true) {

			try {

				//store received packet into byte array
				byte[] buffer = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);

				byte[] data = packet.getData();

				// get byte date into ObjectInputStream
				ByteArrayInputStream in = new ByteArrayInputStream(data);
				ObjectInputStream is = new ObjectInputStream(in);

				// get RoutingTable object
				RoutingTable routingTable = (RoutingTable)is.readObject();
				log.write("Routing table received from " + packet.getAddress().getHostAddress());
				//routingTable.printTable();

				// Check if this routing table's object needs to be updated
				log.write("Starting responder to check for updates");
				Responder respond = new Responder(routingTable, packet.getAddress(), log);
				new Thread(respond).start();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String args[]) {
		try {
			Server startServer = new Server();
			new Thread(startServer).start();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

}
