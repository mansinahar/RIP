/*
 *@author Mansi Nahar 
 */
import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Iterator;

public class RoutingTable implements Serializable {

	public class ForwardingInfo implements Serializable {
		
		private static final long serialVersionUID = 1L;
		public ForwardingInfo(String nextHop, int cost) {
			this.nextHop = nextHop;
			this.cost = cost;
		}
		
		String nextHop;
		int cost;
	}
	
	
	private static final long serialVersionUID = 1L;
	HashMap<InetAddress, ForwardingInfo> routingEntries;
	
	public RoutingTable() {
		try {
		routingEntries = new HashMap<InetAddress, ForwardingInfo>();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * This method adds an entry into the routing table
	 * @param destSubnet
	 * 			destination subnet IP address
	 * @param nextHop
	 * 			nextHop IP address
	 * @param cost
	 * 			Cost to reach the destination
	 */
	public void addEntry(InetAddress destSubnet, String nextHop, int cost) {
		routingEntries.put(destSubnet, new ForwardingInfo(nextHop,cost));
	}
	
	/*
	 * This method prints the routing table entries
	 */
	public void printTable() {
		// creating iterator for HashMap 
		
		System.out.println("Routing table");
		
		Iterator<HashMap.Entry<InetAddress,RoutingTable.ForwardingInfo>> routingEntriesIterator = routingEntries.entrySet().iterator();
		InetAddress destAddress;
		while(routingEntriesIterator.hasNext()) {
			
			HashMap.Entry<InetAddress,RoutingTable.ForwardingInfo> pair = (HashMap.Entry<InetAddress,RoutingTable.ForwardingInfo>)routingEntriesIterator.next();
			destAddress = (InetAddress)pair.getKey();
			ForwardingInfo destForwardingInfo = (ForwardingInfo)pair.getValue();
		
			System.out.print(destAddress + "\t");
			System.out.print(destForwardingInfo.nextHop + "\t");
			System.out.print(destForwardingInfo.cost + "\t");
			System.out.println();
		}
	}
	
	public static void main(String args[]) throws Exception {
		RoutingTable testTable = new RoutingTable();
		try {
		testTable.addEntry(InetAddress.getByName("129.0.0.0"), "129.0.0.0", 20);
		testTable.addEntry(InetAddress.getByName("129.0.0.1"), "129.0.0.1", 6);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		testTable.printTable();
		System.out.println(testTable.routingEntries.containsKey(InetAddress.getByName("129.0.0.0")));
		
	}
	
}
