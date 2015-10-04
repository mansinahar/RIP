import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;

public class RoutingTable implements Serializable {

	private static final long serialVersionUID = 1L;
	ArrayList<InetAddress> destSubnet;
	ArrayList<String> nextHop;
	ArrayList<Integer> cost;
	
	
	public RoutingTable() {
		
		destSubnet = new ArrayList<InetAddress>();
		nextHop = new ArrayList<String>();
		cost = new ArrayList<Integer>();
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
		this.destSubnet.add(destSubnet);
		this.nextHop.add(nextHop);
		this.cost.add(cost);
	}
	
	/*
	 * This method prints the routing table entries
	 */
	public void printTable() {
		// creating iterators for all fields of the table
		Iterator<InetAddress> destSubnetIterator = destSubnet.iterator();
		Iterator<String> nextHopIterator = nextHop.iterator();
		Iterator<Integer> costIterator = cost.iterator();
		
		System.out.println("Destination Subnet\t" + "Next Hop\t" + "Cost");
		
		while(destSubnetIterator.hasNext()) {
			System.out.print(destSubnetIterator.next().getHostAddress() + "\t\t");
			System.out.print(nextHopIterator.next() + "\t");
			System.out.print(costIterator.next() + "\t");
			System.out.println();
		}
	}
	
	public static void main(String args[]) {
		RoutingTable testTable = new RoutingTable();
		try {
		testTable.addEntry(InetAddress.getByName("129.0.0.0"), "129.0.0.0", 20);
		testTable.addEntry(InetAddress.getByName("129.0.0.1"), "129.0.0.1", 6);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		testTable.printTable();
	}
	
}
