/*
 *@author Mansi Nahar 
 */
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;


public class Responder implements Runnable {

	RoutingTable routingTable;
	InetAddress router;
	static Logging log;
	
	public Responder(RoutingTable routingTable, InetAddress router, Logging log) {
		this.routingTable = routingTable;
		this.router = router;
		Responder.log = log;
	}
	
	@Override
	public void run() {
		log.write("In Responder");
		log.write("Calling checkForUpdates method");
		checkForUpdates();
	}
	
	
	/*
	 * This method checks if it's own routing table needs to be updated
	 */
	public void checkForUpdates() {
		synchronized(Client.myRoutingTable){
			
			log.write("*************************************************");
			log.write("In check for updates method");
			log.write("Routing table received from " + router.getHostName());
			
			boolean isUpdated = false;
			InetAddress destAddress;
			int destCost;
			
			//Check if the routing table needs to be updated
			// Iterate through the neighbor's routing table
			Iterator<HashMap.Entry<InetAddress,RoutingTable.ForwardingInfo>> routingEntriesIterator = routingTable.routingEntries.entrySet().iterator();
			
			while(routingEntriesIterator.hasNext()) {
				HashMap.Entry<InetAddress,RoutingTable.ForwardingInfo> pair = (HashMap.Entry<InetAddress,RoutingTable.ForwardingInfo>)routingEntriesIterator.next();
				destAddress = (InetAddress)pair.getKey();
				
				log.write("Checking if my routing table has an entry for " + destAddress.getHostAddress());
				log.write(Client.myRoutingTable.routingEntries.containsKey(destAddress) + "");
				
				
				if(Client.myRoutingTable.routingEntries.containsKey(destAddress)) {
					
					log.write("It does");
					
					destCost = Client.myRoutingTable.routingEntries.get(destAddress).cost;
					
					log.write("Cost for this destination in my routing table is " + destCost);
					log.write("Cost for this destination in received routing table is " + pair.getValue().cost);
					
					if(destCost > (1 + pair.getValue().cost)) {
						log.write("which is smaller than my cost for the destination");
						Client.myRoutingTable.routingEntries.get(destAddress).cost = 1 + pair.getValue().cost;
						Client.myRoutingTable.routingEntries.get(destAddress).nextHop = router.getHostAddress();
						isUpdated = true;
						log.write("Routing table updated for " + destAddress.getHostAddress());
					}
					
				}
			}
			
			//If yes, update it and send updates to all neighbors
			if(isUpdated) {
				Client.sendRoutingTable();
				Client.myRoutingTable.printTable();
			}
		}
		log.write("*************************************************");
	}

	

}
